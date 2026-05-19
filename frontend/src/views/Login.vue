<template>
  <div class="login-container">
    <div class="login-card">
      <h2>🏭 仓库管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-button type="primary" size="large" @click="handleLogin" :loading="loading">登 录</el-button>
      </el-form>
      <div class="demo-info">
        <p>👑 管理员: admin / admin123</p>
        <p>👤 普通用户: user1 / user123</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true

  try {
    const res = await login(form)
    localStorage.setItem('user', JSON.stringify(res.data))
    ElMessage.success('登录成功')
    if (res.data.role === 'admin') {
      router.push('/admin/dashboard')
    } else {
      router.push('/user/home')
    }
  } catch (error) {
    // 错误已由拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
  text-align: center;
}
.login-card h2 {
  margin-bottom: 30px;
  color: #333;
}
.el-button {
  width: 100%;
  margin-top: 10px;
}
.demo-info {
  margin-top: 20px;
  font-size: 12px;
  color: #999;
  text-align: left;
}
</style>