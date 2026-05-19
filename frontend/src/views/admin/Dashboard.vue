<template>
  <div class="dashboard">
    <div class="header">
      <h2>📊 仓库仪表盘</h2>
      <div class="user-info">
        <span>欢迎，{{ user.realName }}</span>
        <el-button type="danger" text @click="logout">退出登录</el-button>
      </div>
    </div>
    <div class="stats">
      <el-card><div class="stat-num">{{ productCount }}</div><div>产品种类</div></el-card>
      <el-card><div class="stat-num">{{ totalStock }}</div><div>总库存量</div></el-card>
      <el-card><div class="stat-num">{{ pendingIn }}</div><div>待确认入库</div></el-card>
      <el-card><div class="stat-num">{{ pendingOut }}</div><div>待确认出库</div></el-card>
    </div>
    <div class="nav-buttons">
      <el-button type="primary" @click="goPage('/admin/product')">📦 产品管理</el-button>
      <el-button type="success" @click="goPage('/admin/stock')">📥 出入库操作</el-button>
      <el-button type="warning" @click="goPage('/admin/user')">👥 用户管理</el-button>
      <el-button type="info" @click="goPage('/admin/record')">📋 记录查询</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductList, getStockInRecords, getStockOutRecords } from '../../api'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const productCount = ref(0)
const totalStock = ref(0)
const pendingIn = ref(0)
const pendingOut = ref(0)

const loadData = async () => {
  const products = await getProductList()
  productCount.value = products.data.length
  totalStock.value = products.data.reduce((sum, p) => sum + p.stockQuantity, 0)

  const stockIn = await getStockInRecords()
  pendingIn.value = stockIn.data.filter(s => s.status === 'pending').length

  const stockOut = await getStockOutRecords()
  pendingOut.value = stockOut.data.filter(s => s.status === 'pending').length
}

const logout = () => {
  localStorage.removeItem('user')
  ElMessage.success('已退出')
  router.push('/login')
}

const goPage = (path) => router.push(path)

onMounted(loadData)
</script>

<style scoped>
.dashboard { padding: 20px; }
.header { display: flex; justify-content: space-between; margin-bottom: 30px; }
.stats { display: grid; grid-template-columns: repeat(4,1fr); gap: 20px; margin-bottom: 30px; }
.stat-num { font-size: 32px; font-weight: bold; color: #409eff; }
.nav-buttons { display: flex; gap: 15px; flex-wrap: wrap; }
.el-card { text-align: center; padding: 20px; cursor: pointer; }
</style>