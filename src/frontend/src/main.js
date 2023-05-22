
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Cookies from "vue-cookies";
const app = createApp(App)
import ElementPlus from 'element-plus'


app.use(ElementPlus)
app.use(router)
app.use(Cookies)
app.mount('#app')
