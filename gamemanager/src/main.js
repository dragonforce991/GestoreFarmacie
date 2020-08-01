import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import './firebase'                            // nuova linea
import VueFire from 'vuefire'                  // nuova linea



//Vue.use(BootstrapVue)
Vue.use(VueFire)                               // nuova linea
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
