import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: () => import('../views/Login.vue') },

    // 管理员路由
    { path: '/admin/dashboard', component: () => import('../views/admin/Dashboard.vue') },
    { path: '/admin/product', component: () => import('../views/admin/ProductManage.vue') },
    { path: '/admin/user', component: () => import('../views/admin/UserManage.vue') },
    { path: '/admin/stock', component: () => import('../views/admin/StockOperation.vue') },
    { path: '/admin/record', component: () => import('../views/admin/RecordQuery.vue') },

    // 普通用户路由
    { path: '/user/home', component: () => import('../views/user/UserHome.vue') },
    { path: '/user/product-query', component: () => import('../views/user/ProductQuery.vue') },
    { path: '/user/stock-in-query', component: () => import('../views/user/StockInQuery.vue') },
    { path: '/user/stock-out-query', component: () => import('../views/user/StockOutQuery.vue') },
    { path: '/user/change-pwd', component: () => import('../views/user/ChangePwd.vue') }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const userStr = localStorage.getItem('user')
    const user = userStr ? JSON.parse(userStr) : null

    if (to.path === '/login') {
        next()
        return
    }

    if (!user) {
        next('/login')
        return
    }

    // 权限检查：普通用户不能访问/admin路径
    if (user.role === 'user' && to.path.startsWith('/admin')) {
        next('/user/home')
        return
    }

    next()
})

export default router