<template>
  <v-container>
    <v-row>
      <v-col cols="7">
        <v-advanced-table table-key="acquisto" :slots="['prodottiConRicetta']" dense outlined v-model="purchases" :columns="headers"  @selected="selected = $event">
          <template v-slot:prodottiConRicetta="{ item }">
            {{ item.prodotti.map(el => el.prodotti.obbligoRicetta).some(el => el) ? 'Si' : 'No' }}
          </template>
        </v-advanced-table>
      </v-col>

      <v-col cols="5">
        <v-row dense>
          <sales-statistics-by-user cols="6" :sales="graficiData" />
          <sales-statistics-by-pharmacy cols="6" :sales="graficiData" />
          <sales-statistics-by-receipt-needed :sales="graficiData" />
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SalesStatisticsByUser from '@/components/statistics/sales_by_user';
import SalesStatisticsByPharmacy from '@/components/statistics/sales_by_pharmacy';
import SalesStatisticsByReceiptNeeded from '@/components/statistics/sales_by_receipt_needed';

export default 
{
  layout: 'dashboard',

  components: {
    SalesStatisticsByReceiptNeeded,
    SalesStatisticsByPharmacy,
    SalesStatisticsByUser
  },

  async asyncData({ $axios })
  {
    const purchases = await $axios.$get('/Acquisti/getAcquisti');

    return {
      purchases,
      selected:[],
      headers:
      [
        {
          text: 'Utente',
          value: 'user.id',
          dataType: 'text',
          groupBy: true,
        },

        {
          text: 'Contiene prodotti con ricetta',
          value: 'prodottiConRicetta',
          computedValue: (item) => item.prodotti.map(el => el.prodotti.obbligoRicetta).some(el => el),
          dataType: 'boolean'
        }
      ]
    }
  },
  computed:{
    graficiData(){
      return this.selected.length == 0 ? this.purchases : this.selected
    }
  },
  mounted()
  {
    this.$store.commit('appBar/setVisible', false);

    if(this.$store.state.user.role.id === '1')
    {
      console.log(this.purchases)
      this.headers = [{
        text: 'Farmacia',
        value: 'idFarmacia',
        dataType: 'text',
        groupBy: true,
      }, ...this.headers]
    }
  }
}
</script>
