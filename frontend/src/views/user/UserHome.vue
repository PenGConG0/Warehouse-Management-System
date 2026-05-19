<template>
  <div class="user-home">
    <div class="header">
      <h2>🏠 仓库管理系统</h2>
      <div class="user-info">
        <span>欢迎，{{ user.realName }}</span>
        <el-button type="danger" text @click="logout">退出登录</el-button>
      </div>
    </div>

    <div class="nav-cards">
      <el-card class="nav-card" @click="goPage('/user/product-query')">
        <div class="card-icon">🔍</div>
        <div class="card-title">库内信息查询</div>
        <div class="card-desc">查看所有产品库存</div>
      </el-card>

      <el-card class="nav-card" @click="goPage('/user/stock-in-query')">
        <div class="card-icon">📥</div>
        <div class="card-title">入库记录查询</div>
        <div class="card-desc">查看历史入库记录</div>
      </el-card>

      <el-card class="nav-card" @click="goPage('/user/stock-out-query')">
        <div class="card-icon">📤</div>
        <div class="card-title">出库记录查询</div>
        <div class="card-desc">查看历史出库记录</div>
      </el-card>

      <el-card class="nav-card" @click="goPage('/user/change-pwd')">
        <div class="card-icon">🔐</div>
        <div class="card-title">修改密码</div>
        <div class="card-desc">更改登录密码</div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')

const logout = () => {
  localStorage.removeItem('user')
  ElMessage.success('已退出')
  router.push('/login')
}

const goPage = (path) => router.push(path)
</script>

<style scoped>
.user-home { padding: 20px; }
.header { display: flex; justify-content: space-between; margin-bottom: 40px; padding-bottom: 20px; border-bottom: 1px solid #eee; }
.nav-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; }
.nav-card { text-align: center; cursor: pointer; transition: transform 0.2s; }
.nav-card:hover { transform: translateY(-5px); }
.card-icon { font-size: 48px; margin: 20px 0 10px; }
.card-title { font-size: 18px; font-weight: bold; margin: 10px 0; }
.card-desc { font-size: 12px; color: #999; margin-bottom: 20px; }
</style>