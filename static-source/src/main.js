import Vue from 'vue'
import App from './App.vue'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'jquery'

import { ToggleButton } from 'vue-js-toggle-button'

Vue.config.productionTip = false

Vue.component('ToggleButton', ToggleButton)

new Vue({
  render: h => h(App),
}).$mount('#app')
