<template>
  <v-col :cols="String(cols)">
    <v-card outlined>
      <v-card-title># Vendite per Utenti</v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-0" style="height: 300px">
        <div v-if="salesByUser.length === 0" class="text-h6 pa-3 text-center">
          Nessun dato disponibile
        </div>

        <CChartPie
          v-else
          style="height: 300px"
          :datasets="[{
							data: salesByUser.map(el => el.sales),
							backgroundColor: colorScheme,
						}]"
          :labels="salesByUser.map(el => el.user)"
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
  name: 'sales-statistics-by-user',

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
    salesByUser()
    {
      if(!this.sales)
        return []

      const mappedSales = groupBy(this.sales, sale => sale.user.id);

      return Array.from(mappedSales).map(([key, value]) => ({ user: key, sales: value.length }));
    }
  },
};
</script>

