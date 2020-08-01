import Vue from 'vue'
import App from './App.vue'
import vuetify from '@/plugins/vuetify' 
Vue.config.productionTip = false
import BootstrapVue from 'bootstrap-vue'
/*import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'*/
Vue.use(BootstrapVue)


new Vue({
  render: h => h(App),
  vuetify
  //router
}).$mount('#app')



/*import VueRouter from 'vue-router'
Vue.use(VueRouter);
import helloworld from '@/components/helloworld.vue'
const router = new VueRouter({
  mode: 'history',
  base:__dirname,
  routes:[
    {path: '/homePage', component: helloworld, alias:'homePage-alias'}
  ]
})*/
