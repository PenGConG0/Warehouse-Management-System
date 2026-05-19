<template>
  <div>
    <div class="header">
      <h2>🔍 库内信息查询</h2>
      <div class="user-info">
        <span>用户：{{ user.realName }}</span>
        <el-button text @click="goHome">返回首页</el-button>
        <el-button type="danger" text @click="logout">退出</el-button>
      </div>
    </div>

    <el-card>
      <el-input v-model="keyword" placeholder="按产品编号/名称搜索" style="width: 300px" clearable @clear="loadData" />
    </el-card>

    <el-table :data="filteredList" border style="margin-top: 20px">
      <el-table-column prop="productCode" label="产品编号" />
      <el-table-column prop="productName" label="产品名称" />
      <el-table-column prop="stockQuantity" label="库存数量" />
      <el-table-column prop="unit" label="单位" />
      <el-table-column prop="location" label="存放位置" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductList } from '../../api'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const productList = ref([])
const keyword = ref('')

const loadData = async () => {
  const res = await getProductList()
  productList.value = res.data
}

const filteredList = computed(() => {
  if (!keyword.value) return productList.value
  const kw = keyword.value.toLowerCase()
  return productList.value.filter(p =>
      p.productCode.toLowerCase().includes(kw) ||
      p.productName.toLowerCase().includes(kw)
  )
})

const goHome = () => router.push('/user/home')
const logout = () => {
  localStorage.removeItem('user')
  ElMessage.success('已退出')
  router.push('/login')
}

onMounted(loadData)
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; }
</style>