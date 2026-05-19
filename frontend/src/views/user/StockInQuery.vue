<template>
  <div>
    <div class="header">
      <h2>📥 入库记录查询</h2>
      <div class="user-info">
        <span>用户：{{ user.realName }}</span>
        <el-button text @click="goHome">返回首页</el-button>
        <el-button type="danger" text @click="logout">退出</el-button>
      </div>
    </div>

    <el-card>
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索产品名称/编号" style="width: 250px" clearable />
        <el-select v-model="statusFilter" placeholder="状态" clearable style="width: 120px; margin-left: 10px">
          <el-option label="全部" value="" />
          <el-option label="待确认" value="pending" />
          <el-option label="已确认" value="confirmed" />
        </el-select>
        <el-button type="primary" @click="loadData" style="margin-left: 10px">查询</el-button>
      </div>

      <el-table :data="filteredList" border style="margin-top: 20px">
        <el-table-column prop="id" label="单据号" width="80" />
        <el-table-column label="产品信息" width="200">
          <template #default="{ row }">
            <div>{{ getProductCode(row.productId) }}</div>
            <div style="font-size: 12px; color: #999">{{ getProductName(row.productId) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="入库数量" width="100" />
        <el-table-column label="操作时间" width="180" show-overflow-tooltip>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductList, getStockInRecords } from '../../api'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const productList = ref([])
const stockInRecords = ref([])
const keyword = ref('')
const statusFilter = ref('')

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

const filteredList = computed(() => {
  let list = stockInRecords.value
  if (keyword.value) {
    const kw = keyword.value.toLowerCase()
    list = list.filter(r => {
      const name = getProductName(r.productId).toLowerCase()
      const code = getProductCode(r.productId).toLowerCase()
      return name.includes(kw) || code.includes(kw)
    })
  }
  if (statusFilter.value) {
    list = list.filter(r => r.status === statusFilter.value)
  }
  return list
})

const loadData = async () => {
  const [productsRes, recordsRes] = await Promise.all([
    getProductList(),
    getStockInRecords()
  ])
  productList.value = productsRes.data
  stockInRecords.value = recordsRes.data
}

const goHome = () => router.push('/user/home')
const logout = () => {
  localStorage.removeItem('user')
  ElMessage.success('已退出')
  router.push('/login')
}

onMounted(loadData)
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; align-items: center; }
.search-bar { display: flex; align-items: center; }
</style>