<template>
  <v-col :cols="String(cols)">
    <v-card outlined>
      <v-card-title># Vendite per Richiesta Ricetta</v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-0" style="height: 300px">
        <div v-if="salesByReceiptNeeded.length === 0" class="text-h6 pa-3 text-center">
          Nessun dato disponibile
        </div>

        <CChartPie
          v-else
          style="height: 300px"
          :datasets="[{
							data: salesByReceiptNeeded.map(el => el.sales),
							backgroundColor: colorScheme,
						}]"
          :labels="salesByReceiptNeeded.map(el => el.receipt)"
          :options="{
						maintainAspectRatio: false,
					}"
        />
      </v-card-text>
    </v-card>
  </v-col>
</template>

<script>
import { groupBy } from '@/plugins/statistics';

export default
{
  name: 'sales-statistics-by-receipt-needed',

  props: {
    sales: { required: true },
    cols: { type: String, default: '12' }
  },

  data()
  {
    return {
      colorScheme: []
    }
  },

  mounted()
  {
    this.colorScheme = new ColorScheme()
      .from_hue(Math.floor(Math.random() * 100) + 1)
      .scheme('triade')
      .distance(0.51)
      .web_safe(true)
      .colors()
      .map(el => '#' + el);
  },

  computed:
  {
    salesByReceiptNeeded()
    {
      if(!this.sales)
        return []

      const mappedSales = groupBy(this.sales, sale => sale.prodotti.map(el => el.prodotti.obbligoRicetta).some(el => el));

      return Array.from(mappedSales).map(([key, value]) => ({ receipt: key ? 'Richiesta' : 'Non Richiesta', sales: value.length }));
    }
  },
};
</script>

