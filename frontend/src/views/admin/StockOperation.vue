<template>
  <div>
    <div class="header">
      <h2>📥 出入库操作</h2>
      <el-button text @click="goBack">返回</el-button>
    </div>

    <el-tabs v-model="activeTab">
      <!-- 入库登记 -->
      <el-tab-pane label="入库登记" name="in">
        <el-card>
          <el-form :model="stockInForm" label-width="100px">
            <el-form-item label="选择产品">
              <el-select v-model="stockInForm.productId" placeholder="请选择产品" filterable>
                <el-option
                    v-for="p in productList"
                    :key="p.id"
                    :label="`${p.productCode} - ${p.productName} (库存: ${p.stockQuantity})`"
                    :value="p.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="入库数量">
              <el-input-number v-model="stockInForm.quantity" :min="1" :max="9999" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="stockInForm.remark" type="textarea" rows="2" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitStockIn" :loading="inLoading">登记入库</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 出库登记 -->
      <el-tab-pane label="出库登记" name="out">
        <el-card>
          <el-form :model="stockOutForm" label-width="100px">
            <el-form-item label="选择产品">
              <el-select v-model="stockOutForm.productId" placeholder="请选择产品" filterable>
                <el-option
                    v-for="p in productList"
                    :key="p.id"
                    :label="`${p.productCode} - ${p.productName} (库存: ${p.stockQuantity})`"
                    :value="p.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="出库数量">
              <el-input-number v-model="stockOutForm.quantity" :min="1" :max="Math.max(getMaxStock(), 1)" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="stockOutForm.remark" type="textarea" rows="2" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="submitStockOut" :loading="outLoading">登记出库</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 待确认列表 -->
      <el-tab-pane label="待确认单据" name="pending">
        <el-card style="margin-top: 20px">
          <template #header><span>待确认入库</span></template>
          <el-table :data="pendingStockIn" border>
            <el-table-column prop="id" label="单据号" width="80" />
            <el-table-column label="产品名称">
              <template #default="{ row }">
                {{ getProductName(row.productId) }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" />
            <el-table-column label="登记时间" width="180" show-overflow-tooltip>
              <template #default="{ row }">
                {{ formatDate(row.operateTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="confirmStockInHandler(row.id)">确认入库</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <el-card style="margin-top: 20px">
          <template #header><span>待确认出库</span></template>
          <el-table :data="pendingStockOut" border>
            <el-table-column prop="id" label="单据号" width="80" />
            <el-table-column label="产品名称">
              <template #default="{ row }">
                {{ getProductName(row.productId) }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" />
            <el-table-column label="登记时间" width="180" show-overflow-tooltip>
              <template #default="{ row }">
                {{ formatDate(row.operateTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="success" size="small" @click="confirmStockOutHandler(row.id)">确认出库</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, stockIn, stockOut, confirmStockIn, confirmStockOut, getStockInRecords, getStockOutRecords } from '../../api'

const router = useRouter()
const activeTab = ref('in')
const inLoading = ref(false)
const outLoading = ref(false)
const productList = ref([])
const stockInForm = ref({ productId: null, quantity: 1, remark: '' })
const stockOutForm = ref({ productId: null, quantity: 1, remark: '' })
const pendingStockIn = ref([])
const pendingStockOut = ref([])

const getMaxStock = () => {
  const product = productList.value.find(p => p.id === stockOutForm.value.productId)
  return product ? product.stockQuantity : 0
}

const getProductName = (id) => {
  const product = productList.value.find(p => p.id === id)
  return product ? `${product.productCode} - ${product.productName}` : '未知产品'
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

const loadProducts = async () => {
  const res = await getProductList()
  productList.value = res.data
}

const loadPendingRecords = async () => {
  const [inRes, outRes] = await Promise.all([
    getStockInRecords(),
    getStockOutRecords()
  ])
  pendingStockIn.value = inRes.data.filter(r => r.status === 'pending')
  pendingStockOut.value = outRes.data.filter(r => r.status === 'pending')
}

const submitStockIn = async () => {
  if (!stockInForm.value.productId) {
    ElMessage.warning('请选择产品')
    return
  }
  inLoading.value = true
  try {
    await stockIn(stockInForm.value)
    ElMessage.success('入库登记成功，等待管理员确认')
    stockInForm.value = { productId: null, quantity: 1, remark: '' }
    loadPendingRecords()
  } catch (error) {
    // 错误已处理
  } finally {
    inLoading.value = false
  }
}

const submitStockOut = async () => {
  if (!stockOutForm.value.productId) {
    ElMessage.warning('请选择产品')
    return
  }
  outLoading.value = true
  try {
    await stockOut(stockOutForm.value)
    ElMessage.success('出库登记成功，等待管理员确认')
    stockOutForm.value = { productId: null, quantity: 1, remark: '' }
    loadPendingRecords()
  } catch (error) {
    // 错误已处理
  } finally {
    outLoading.value = false
  }
}

const confirmStockInHandler = (id) => {
  ElMessageBox.confirm('确认入库后库存将增加，确定吗？', '提示').then(async () => {
    await confirmStockIn(id)
    ElMessage.success('确认入库成功')
    loadPendingRecords()
    loadProducts()
  })
}

const confirmStockOutHandler = (id) => {
  ElMessageBox.confirm('确认出库后库存将减少，确定吗？', '提示').then(async () => {
    await confirmStockOut(id)
    ElMessage.success('确认出库成功')
    loadPendingRecords()
    loadProducts()
  })
}

const goBack = () => router.back()

onMounted(() => {
  loadProducts()
  loadPendingRecords()
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; align-items: center; }
.el-card { margin-top: 20px; }
</style>