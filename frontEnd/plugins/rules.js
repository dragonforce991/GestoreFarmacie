export default (context, inject) =>
{
  const rules = {
    basicRules: [v => !!v || 'Campo richiesto'],
    emailRules: [
      v => !!v || 'Campo richiesto',
      v => v && /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/.test(v) || 'Email non valida'
    ],
    basicCurrencyRules: [
      v => !!v || 'Campo richiesto',
      v => v && Number(v.toString().replace(/[^0-9.]+/g, "")) > 0 || 'Valore maggiore a 0'
    ],
    basicNumberRules: [
      v => !!v || 'Campo richiesto',
      v => v > 0 || 'Valore maggiore a 0'
    ],
  }

  inject('rules', rules)
}
