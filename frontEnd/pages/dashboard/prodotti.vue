<template>
  <v-container>
    <v-advanced-table ref="table" @selected="selected = $event" dense outlined :columns="headers" v-model="movements" :slots="['product.obbligoRicetta']">
      <template v-slot:product.obbligoRicetta="{ item }">
        {{ item.product.obbligoRicetta == true ? "NO" : "SI"}}
      </template>
    </v-advanced-table>
      
    <v-dialog v-model="dialog" width="700" :persistent="loading">
      <v-card>
        <v-toolbar class="elevation-0 white--text" color="primary">
          <v-btn color="white" :disabled="loading" @click="dialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuovo Movimento</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!validForm" :loading="loading" @click="create">Crea</v-btn>
        </v-toolbar>

        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-col cols="3">
                <v-checkbox class="mt-1" label="Nuovo Prodotto" v-model="newProduct" />
              </v-col>

              <v-col cols="9" v-if="!newProduct">
                <v-combobox
                  :rules="$rules.basicRules"
                  outlined dense label="Prodotto"
                  v-model="movement.product" :items="products"
                  item-text="nome" item-value="idProdotto" />
              </v-col>

              <v-col cols="12">
                <v-text-field
                  :rules="$rules.basicRules"
                  type="number" dense outlined
                  label="Quantità" v-model.number="movement.quantita" />
              </v-col>

              <v-row dense v-if="newProduct">
                <v-col cols="12">
                  <v-banner>Dati nuovo prodotto</v-banner>
                </v-col>

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
                  <v-combobox :rules="$rules.basicRules" v-model="product.ParoleChiave" :items="[]" hide-selected dense outlined label="Parole Chiave" multiple persistent-hint small-chips clearable>
                    <template v-slot:no-data>
                      <v-list-item>
                        <v-list-item-content>
                          <v-list-item-title>
                            Premi <kbd>invio</kbd> per aggiungere un elemento
                          </v-list-item-title>
                        </v-list-item-content>
                      </v-list-item>
                    </template>
                  </v-combobox>
              </v-col>

                <v-col cols="12">
                  <v-textarea dense outlined label="Descrizione" v-model="movement.Descrizione"></v-textarea>
                </v-col>
              </v-row>
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
  layout: 'dashboard',

  components: {
    Import_dialog
  },

  async asyncData({ $axios })
  {
    const movements = await $axios.$get('/Magazzino/getMagazzino');
    const products = await $axios.$get('/Product/getProducts');
    console.log(products);
    console.log(movements);


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
          text: 'Prezzo',
          value: 'product.costoUnitario',
          dataType: 'currency'
        },
        {
          text: 'Codice',
          value: 'product.codice',
          dataType: 'text',
          caseSensitiveSelector: true,
        },
        {
          text: 'Ricetta',
          value: 'product.obbligoRicetta',
          dataType: 'boolean'
        },
        {
          text: 'Qtà.',
          value: 'quantita',
          groupBy: true,
          dataType: 'number'
        },
      ],

      products,
      movements
    }
  },

  data()
  {
    return {
      movement: {},
      selected: [],

      newProduct: false, product: {},

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
      this.movement = this.product = {};
    },

    async create()
    {
      this.loading = true

      try
      {
        this.product.ObbligoRicetta = !!this.product.ObbligoRicetta

        if(this.newProduct)
        {
          this.product.idProdotto = await this.$axios.$post('/Product/insertProduct', this.product);
          this.products.push(this.product)
          this.movement.product = this.product
        }

        await this.$axios.$post('Magazzino/insertProduct', {
          idProdotto: this.movement.product.idProdotto,
          quantita: this.movement.quantita
        });

        this.movements.push(this.movement);
        this.$notifier.showInfo('Movimento creato con successo');

        this.loading = this.dialog = false;
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
