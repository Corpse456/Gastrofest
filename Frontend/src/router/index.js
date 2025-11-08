import {createRouter, createWebHistory} from 'vue-router'
import MainTable from '@/components/MainTable.vue'
import MealPage from '@/components/MealPage.vue'

const routes = [
    {path: '/', name: 'MainTable', component: MainTable},
    {path: '/meals/:id', name: 'MealPage', component: MealPage},
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
