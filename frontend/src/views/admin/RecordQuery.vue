<template>
  <div>
    <div class="header">
      <h2>📋 出入库记录查询</h2>
      <el-button text @click="goBack">返回</el-button>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="入库记录" name="in">
        <el-card>
          <div class="search-bar">
            <el-input v-model="inKeyword" placeholder="搜索产品名称/编号" style="width: 250px" clearable />
            <el-select v-model="inStatus" placeholder="状态" clearable style="width: 120px; margin-left: 10px">
              <el-option label="全部" value="" />
              <el-option label="待确认" value="pending" />
              <el-option label="已确认" value="confirmed" />
            </el-select>
            <el-button type="primary" @click="loadStockInRecords" style="margin-left: 10px">查询</el-button>
          </div>
          <el-table :data="filteredStockIn" border style="margin-top: 20px">
            <el-table-column prop="id" label="单据号" width="80" />
            <el-table-column label="产品信息" width="200">
              <template #default="{ row }">
                <div>{{ getProductCode(row.productId) }}</div>
                <div style="font-size: 12px; color: #999">{{ getProductName(row.productId) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="入库数量" width="100" />
            <el-table-column label="登记时间" width="180" show-overflow-tooltip>
              <template #default="{ row }">
                {{ formatDate(row.operateTime) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'confirmed' ? 'success' : 'warning'">
                  {{ row.status === 'confirmed' ? '已确认' : '待确认' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="出库记录" name="out">
        <el-card>
          <div class="search-bar">
            <el-input v-model="outKeyword" placeholder="搜索产品名称/编号" style="width: 250px" clearable />
            <el-select v-model="outStatus" placeholder="状态" clearable style="width: 120px; margin-left: 10px">
              <el-option label="全部" value="" />
              <el-option label="待确认" value="pending" />
              <el-option label="已确认" value="confirmed" />
            </el-select>
            <el-button type="primary" @click="loadStockOutRecords" style="margin-left: 10px">查询</el-button>
          </div>
          <el-table :data="filteredStockOut" border style="margin-top: 20px">
            <el-table-column prop="id" label="单据号" width="80" />
            <el-table-column label="产品信息" width="200">
              <template #default="{ row }">
                <div>{{ getProductCode(row.productId) }}</div>
                <div style="font-size: 12px; color: #999">{{ getProductName(row.productId) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="出库数量" width="100" />
            <el-table-column label="登记时间" width="180" show-overflow-tooltip>
              <template #default="{ row }">
                {{ formatDate(row.operateTime) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'confirmed' ? 'success' : 'warning'">
                  {{ row.status === 'confirmed' ? '已确认' : '待确认' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductList, getStockInRecords, getStockOutRecords } from '../../api'

const router = useRouter()
const activeTab = ref('in')
const productList = ref([])
const stockInRecords = ref([])
const stockOutRecords = ref([])
const inKeyword = ref('')
const outKeyword = ref('')
const inStatus = ref('')
const outStatus = ref('')

const getProductName = (id) => {
  const product = productList.value.find(p => p.id === id)
  return product ? product.productName : '未知'
}

const getProductCode = (id) => {
  const product = productList.value.find(p => p.id === id)
  return product ? product.productCode : '未知'
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

const filteredStockIn = computed(() => {
  let list = stockInRecords.value
  if (inKeyword.value) {
    const kw = inKeyword.value.toLowerCase()
    list = list.filter(r => {
      const name = getProductName(r.productId).toLowerCase()
      const code = getProductCode(r.productId).toLowerCase()
      return name.includes(kw) || code.includes(kw)
    })
  }
  if (inStatus.value) {
    list = list.filter(r => r.status === inStatus.value)
  }
  return list
})

const filteredStockOut = computed(() => {
  let list = stockOutRecords.value
  if (outKeyword.value) {
    const kw = outKeyword.value.toLowerCase()
    list = list.filter(r => {
      const name = getProductName(r.productId).toLowerCase()
      const code = getProductCode(r.productId).toLowerCase()
      return name.includes(kw) || code.includes(kw)
    })
  }
  if (outStatus.value) {
    list = list.filter(r => r.status === outStatus.value)
  }
  return list
})

const loadProducts = async () => {
  const res = await getProductList()
  productList.value = res.data
}

const loadStockInRecords = async () => {
  const res = await getStockInRecords()
  stockInRecords.value = res.data
}

const loadStockOutRecords = async () => {
  const res = await getStockOutRecords()
  stockOutRecords.value = res.data
}

const goBack = () => router.back()

onMounted(async () => {
  await loadProducts()
  await Promise.all([loadStockInRecords(), loadStockOutRecords()])
})
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; align-items: center; }
.search-bar { display: flex; align-items: center; }
</style>