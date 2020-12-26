import Vue from 'vue'

Vue.filter('formatCurrency', val =>
{
  if(typeof val !== "number")
    val = Number(val);

  if(!val)
    val = 0;

  return '€ ' + val.toFixed(2).replace(".", ",").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
})
