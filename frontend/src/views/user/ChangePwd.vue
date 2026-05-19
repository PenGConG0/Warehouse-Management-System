<template>
  <div>
    <h2>🔐 修改密码</h2>
    <el-button text @click="goBack" style="margin-bottom: 20px">← 返回</el-button>
    <el-card style="width: 400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="新密码"><el-input v-model="form.newPwd" type="password" /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="form.confirmPwd" type="password" /></el-form-item>
        <el-button type="primary" @click="changePwd">确认修改</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { changePassword } from '../../api'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const form = reactive({ newPwd: '', confirmPwd: '' })

const changePwd = async () => {
  if (form.newPwd !== form.confirmPwd) {
    return ElMessage.error('两次密码不一致')
  }
  if (form.newPwd.length < 3) {
    return ElMessage.error('密码至少3位')
  }
  await changePassword(user.id, form.newPwd)
  ElMessage.success('密码修改成功，请重新登录')
  localStorage.removeItem('user')
  router.push('/login')
}

const goBack = () => router.back()
</script>