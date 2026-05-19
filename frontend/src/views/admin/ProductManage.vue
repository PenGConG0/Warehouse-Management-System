<template>
  <div>
    <div class="header">
      <h2>📦 产品管理</h2>
      <el-button type="primary" @click="openAddDialog">+ 添加产品</el-button>
      <el-button text @click="goBack">返回</el-button>
    </div>

    <el-table :data="productList" border>
      <el-table-column prop="productCode" label="产品编号" />
      <el-table-column prop="productName" label="产品名称" />
      <el-table-column prop="stockQuantity" label="库存数量" />
      <el-table-column prop="unit" label="单位" />
      <el-table-column prop="location" label="存放位置" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="addDialog" title="添加产品">
      <el-form :model="newProduct" label-width="100px">
        <el-form-item label="产品编号"><el-input v-model="newProduct.productCode" /></el-form-item>
        <el-form-item label="产品名称"><el-input v-model="newProduct.productName" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="newProduct.unit" placeholder="个/台/箱" /></el-form-item>
        <el-form-item label="存放位置"><el-input v-model="newProduct.location" /></el-form-item>
        <el-form-item label="初始库存"><el-input-number v-model="newProduct.stockQuantity" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialog = false">取消</el-button>
        <el-button type="primary" @click="addProduct">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, addProduct as apiAddProduct, deleteProduct } from '../../api'

const router = useRouter()
const productList = ref([])
const addDialog = ref(false)
const newProduct = ref({
  productCode: '', productName: '', unit: '个', location: '', stockQuantity: 0
})

const loadProducts = async () => {
  const res = await getProductList()
  productList.value = res.data
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除 ${row.productName} 吗？`, '提示').then(async () => {
    await deleteProduct(row.id)
    ElMessage.success('删除成功')
    loadProducts()
  })
}

const addProduct = async () => {
  if (!newProduct.value.productCode || !newProduct.value.productName) {
    ElMessage.warning('请填写完整信息')
    return
  }
  await apiAddProduct(newProduct.value)
  ElMessage.success('添加成功')
  addDialog.value = false
  newProduct.value = { productCode: '', productName: '', unit: '个', location: '', stockQuantity: 0 }
  loadProducts()
}

const openAddDialog = () => addDialog.value = true
const goBack = () => router.back()

onMounted(loadProducts)
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; align-items: center; }
</style>