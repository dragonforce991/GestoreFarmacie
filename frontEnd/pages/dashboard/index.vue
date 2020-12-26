<template>
  <v-container fluid>
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
          <sales-statistics-by-receipt-needed cols="6" :sales="graficiData" />
          <sales-statistics-by-pharmacy :sales="graficiData" v-if="isRegion" />
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

  async asyncData({ $axios, store })
  {
    const purchases = await $axios.$get('/Acquisti/getAcquisti');

    var headers = [ 
        {
          text: 'Utente',
          value: 'user.full_name',
          dataType: 'text',
          groupBy: true,
          caseSensitiveSelector:true
        },
        {
          text: 'Data Vendita',
          value: 'dataAcquisto',
          dataType: 'date',
        },
        {
          text: 'Contiene prodotti con ricetta',
          value: 'prodottiConRicetta',
          computedValue: (item) => item.prodotti.map(el => el.prodotti.obbligoRicetta).some(el => el),
          dataType: 'boolean'
        },
        {
          text: 'Totale',
          value: 'totale',
          dataType : 'number'
        }
      ]
    if(store.state.user.role.id == '1'){
          headers.splice(0,0,
            {
              text: 'Farmacia',
              value: 'farmacia.nome',
              dataType: 'text',
              groupBy: true,
              caseSensitiveSelector:true
            })
    }
    return {
      purchases,
      selected:[],
      headers
    }
    
  },
  computed:{
    isRegion(){
      return this.$store.state.user.role.id == '1';
    },
    graficiData(){
      return this.selected.length == 0 ? this.purchases : this.selected
    }
  },
  mounted()
  {
    this.$store.commit('appBar/setVisible', false);
  }
}
</script>
