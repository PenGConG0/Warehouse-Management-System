<template>
  <div>
    <div class="header">
      <h2>👥 用户管理</h2>
      <el-button type="primary" @click="openAddDialog">+ 添加用户</el-button>
      <el-button text @click="goBack">返回</el-button>
    </div>

    <el-table :data="userList" border>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="role" label="角色">
        <template #default="{ row }">
          <el-tag :type="row.role === 'admin' ? 'danger' : 'success'">
            {{ row.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="handleDelete(row)" :disabled="row.username === 'admin'">删除</el-button>
          <el-button type="warning" size="small" @click="resetPwd(row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="addDialog" title="添加用户">
      <el-form :model="newUser" label-width="100px">
        <el-form-item label="用户名"><el-input v-model="newUser.username" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="newUser.realName" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="newUser.role">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialog = false">取消</el-button>
        <el-button type="primary" @click="addUser">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, addUser as apiAddUser, deleteUser, changePassword } from '../../api'

const router = useRouter()
const userList = ref([])
const addDialog = ref(false)
const newUser = ref({ username: '', realName: '', role: 'user' })

const loadUsers = async () => {
  const res = await getUserList()
  userList.value = res.data
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除用户 ${row.realName} 吗？`, '提示').then(async () => {
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    loadUsers()
  })
}

const resetPwd = (row) => {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then(async ({ value }) => {
    if (value && value.length >= 3) {
      await changePassword(row.id, value)
      ElMessage.success('密码已重置')
    } else {
      ElMessage.warning('密码长度至少3位')
    }
  })
}

const addUser = async () => {
  if (!newUser.value.username || !newUser.value.realName) {
    ElMessage.warning('请填写完整信息')
    return
  }
  await apiAddUser(newUser.value)
  ElMessage.success('添加成功，默认密码123456')
  addDialog.value = false
  newUser.value = { username: '', realName: '', role: 'user' }
  loadUsers()
}

const openAddDialog = () => addDialog.value = true
const goBack = () => router.back()

onMounted(loadUsers)
</script>

<style scoped>
.header { display: flex; justify-content: space-between; margin-bottom: 20px; align-items: center; }
</style>