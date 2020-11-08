<template>
  <v-container>
    <v-advanced-table ref="table" @selected="selected = $event" dense outlined :columns="headers" v-model="products" />

    <v-dialog v-model="dialog" width="700" :persistent="loading">
      <v-card>
        <v-toolbar class="elevation-0 white--text" color="primary">
          <v-btn color="white" @click="dialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuovo Prodotto</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!validForm" :loading="loading" @click="create">Crea</v-btn>
        </v-toolbar>

        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-col cols="12">
                <v-checkbox dense outlined label="Obbligo Ricetta" v-model="product.ObbligoRicetta"></v-checkbox>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" type="number" dense outlined label="Costo Unitario" v-model.number="product.CostoUnitario"></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Nome" v-model="product.Nome"></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Azienda" v-model="product.Azienda"></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Codice" v-model="product.Codice"></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-textarea dense outlined label="Descrizione" v-model="product.Descrizione"></v-textarea>
              </v-col>
            </v-row>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>

    <import_dialog ref="importDialog" />
  </v-container>
</template>

<script>
import Import_dialog from '@/components/products/import_dialog';

export default
{
  components: {Import_dialog},
  layout: 'dashboard',

  async asyncData({ $axios })
  {
    const products = await $axios.$get('/Magazzino/getMagazzino');

    return {
      headers:
      [
        {
          text: 'Prodotto',
          value: 'product.nome',
          dataType: 'text',
          caseSensitiveSelector: true,
        },

        {
          text: 'Descrizione',
          value: 'product.descizione',
          dataType: 'text',
          caseSensitiveSelector: true,
        },

        {
          text: 'Qtà.',
          value: 'quantita',
          groupBy: true,
          dataType: 'number'
        },
      ],

      products,
    }
  },

  data()
  {
    return {
      product: {},
      selected: [],

      loading: false, dialog: false, validForm: false
    }
  },

  watch:
  {
    selected(val) {
      this.$store.commit('appBar/setSelectedRows', val.length)
    }
  },

  mounted()
  {
    this.$store.commit('appBar/setVisible', true);
    this.$store.commit('appBar/setButtons', {
      addButton: this.$store.state.user.role.id == 2,
      excelButton: true,
      importButton: true,
    });

    this.focusSearchUnsubscriber = this.$store.subscribe((mutation) =>
    {
      switch (mutation.type)
      {
        case 'appBar/addClicked': this.add();	break;
        case 'appBar/excelClicked': this.$refs.table ? this.$refs.table.exportToExcel('Prodotti') : null;	break;
        case 'appBar/importClicked': this.$refs.importDialog ? this.$refs.importDialog.show() : null;	break;
      }
    });
  },

  methods:
  {
    add()
    {
      this.dialog = true;
      this.product = {};
    },

    async create()
    {
      this.loading = true

      try
      {
        this.product.ParoleChiave = []

        const response = await this.$axios.$post('/Product/insertProduct', this.product);
        this.product = {...this.product, id : response}
        this.products.push(this.product);
        this.$notifier.showInfo('Prodotto creato con successo');
        this.loading = false;
        this.dialog = false;
      }
      catch (e)
      {
        console.error(e);
        // this.$nuxt.error(e)
      }
    }
  }
};
</script>
