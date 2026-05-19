import request from '../utils/request'

// 认证
export const login = (data) => request.post('/auth/login', data)
export const logout = () => request.post('/auth/logout')
export const getCurrentUser = () => request.get('/auth/current')

// 产品
export const getProductList = () => request.get('/product/list')
export const addProduct = (data) => request.post('/product/add', data)
export const deleteProduct = (id) => request.delete(`/product/delete/${id}`)

// 出入库
export const stockIn = (data) => request.post('/stock/in', data)
export const stockOut = (data) => request.post('/stock/out', data)
export const confirmStockIn = (id) => request.put(`/stock/in/confirm/${id}`)
export const confirmStockOut = (id) => request.put(`/stock/out/confirm/${id}`)

// 用户
export const getUserList = () => request.get('/user/list')
export const addUser = (data) => request.post('/user/add', data)
export const deleteUser = (id) => request.delete(`/user/delete/${id}`)
export const changePassword = (id, newPassword) =>
    request.put(`/user/password?id=${id}&newPassword=${newPassword}`)

// 记录
export const getStockInRecords = () => request.get('/record/in')
export const getStockOutRecords = () => request.get('/record/out')