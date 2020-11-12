<template>
  <v-container>
    <v-advanced-table table-key="acquisto" ref="table" :slots="['prodottiConRicetta']" dense outlined v-model="purchases" :columns="headers">
      <template v-slot:prodottiConRicetta="{ item }">
        {{ item.prodotti.map(el => el.prodotti.obbligoRicetta).some(el => el) ? 'Si' : 'No' }}
      </template>
    </v-advanced-table>

    <v-dialog v-model="newCustomerDialog" :persistent="loadingPatient" width="500">
      <v-card>
        <v-toolbar color="primary white--text" class="elevation-0">
          <v-btn color="white" @click="newCustomerDialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuovo Paziente</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!validForm" :loading="loadingPatient" @click="createPatient">Crea</v-btn>
        </v-toolbar>

        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-col cols="12">
                <v-text-field
                  v-mask="'AAAAAA##A##A###A'"
                  :rules="$rules.fiscalCodeRule"
                  dense outlined label="Codice Fiscale"
                  v-model="patient.codice_Fiscale"
                  counter="16"
                />
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Nome" v-model="patient.nome"></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Cognome" v-model="patient.cognome"></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Telefono" v-model="patient.telefono"></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialog" width="1200" :persistent="loading">
      <v-card>
        <v-toolbar class="elevation-0 white--text" color="primary">
          <v-btn color="white" :disabled="loading" @click="dialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuova Vendita</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!valid" :loading="loading" @click="create">Crea</v-btn>
        </v-toolbar>

        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-col cols="1">
                <v-btn color="primary" icon @click="newCustomerDialog = true">
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </v-col>

              <v-col cols="11">
                <v-combobox
                  :rules="$rules.basicRules"
                  outlined dense label="Paziente"
                  v-model="sale.patient" :items="patients"
                  item-text="nome" item-value="idProdotto" />
              </v-col>

              <v-col cols="12">
                <v-simple-table fixed-header>
                  <thead>
                    <tr>
                      <th class="text-left subtitle-2">Prodotto</th>
                      <th class="text-left subtitle-2">Obb. Ricetta</th>
                      <th class="text-left subtitle-2">Quantità</th>
                      <th class="text-left subtitle-2">Codice Regionale</th>
                      <th class="text-left subtitle-2">Data Ricetta</th>
                      <th class="text-left subtitle-2">Costo Unitario</th>
                      <th class="text-left subtitle-2">Costo Totale</th>
                      <th class="text-left subtitle-2"></th>
                    </tr>
                  </thead>

                  <tbody>
                    <tr v-for="(element, index) in sale.products" :key="index">
                      <td v-if="!element" colspan=7>
                        <v-select :rules="$rules.basicRules" v-model="sale.products[index]" :items="availableProducts" item-text="nome" return-object></v-select>
                      </td>

                      <td v-if="element" width="30%"> <!-- Prodotto -->
                        <v-text-field readonly :rules="$rules.basicRules" v-model="element.nome"></v-text-field>
                      </td>

                      <td v-if="element"> <!-- Ricetta -->
                        <v-checkbox readonly v-model="element.obbligoRicetta" color="primary" dense />
                      </td>

                      <td v-if="element"> <!-- Qty -->
                        <v-text-field
                          :rules="$rules.maxNumberRules(maxQuantities[element.idProdotto])"
                          min="1" step="1"
                          :max="maxQuantities[element.idProdotto]"
                          :hint="maxQuantities[element.idProdotto].toString()"
                          persistent-hint
                          type="number" v-model.number="element.quantity"
                          @input="calcTotal(element)" />
                      </td>

                      <td v-if="element"> <!-- Codice -->
                        <v-text-field
                          :rules="element.obbligoRicetta ? $rules.basicRules : []"
                          v-model="element.regionalCode" />
                      </td>

                        <td v-if="element"> <!-- Date -->
                          <v-advanced-date-picker
                            @valid="$set(validDate, index, $event)"
                            v-model="element.date"
                            :mandatory="element.obbligoRicetta" />
                        </td>

                      <td v-if="element"> <!-- Costo Unitario  -->
                        <v-text-field readonly :value="element.costoUnitario | formatCurrency"></v-text-field>
                      </td>

                      <td v-if="element"> <!-- Costo Totale -->
                        <v-text-field readonly :value="element.total | formatCurrency"></v-text-field>
                      </td>

                      <td class="text-center">
                        <v-btn @click="removeManpower(index)" color="red" icon text>
                          <v-icon size="20px">mdi-delete</v-icon>
                        </v-btn>
                      </td>
                    </tr>

                    <td colspan="8" align="center">
                      <v-btn color="primary" @click="sale.products.push(null)" class="mt-3" outlined>Aggiungi</v-btn>
                    </td>
                  </tbody>

                  <tfoot align="right">
                    <tr>
                      <td colspan="9" class="text-xs-right"><b style="font-size: 16px">Sub Totale: {{ total | formatCurrency }}</b></td>
                    </tr>
                  </tfoot>
                </v-simple-table>
              </v-col>
            </v-row>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { mask } from 'vue-the-mask'

export default 
{
  layout: 'dashboard',

  directives: {
    mask,
  },

  data()
  {
    return {
      selected: [],

      newSale: false, sale: { products: [] },

      loading: false, dialog: false, loadingPatient: false,
      newCustomerDialog: false,
      validForm: false, validDate: {}
    }
  },

  async asyncData({ $axios })
  {
    const purchases = await $axios.$get('/Acquisti/getAcquisti');
    const patients = await $axios.$get('/Pazienti/getPazienti');
    const warehouse = await $axios.$get('/Magazzino/getMagazzino');
    console.log(purchases);
    return {
      purchases,
      products: warehouse.map(el => el.product),
      maxQuantities: warehouse.reduce((map, obj) =>
      {
        map[obj.idProdotto] = obj.quantita;
        return map;
      }, {}),
      patients,
      patient: {},

      headers:
      [
        {
          text: 'Data Vendita',
          value: 'dataAcquisto',
          dataType: 'date',
        },
        {
          text: 'Utente',
          value: 'user.full_name',
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
      addButton: true,
      excelButton: true,
    });

    this.focusSearchUnsubscriber = this.$store.subscribe((mutation) =>
    {
      switch (mutation.type)
      {
        case 'appBar/addClicked': this.add();	break;
        case 'appBar/excelClicked': this.$refs.table ? this.$refs.table.exportToExcel('Prodotti') : null;	break;
      }
    });
  },

  computed:
  {
    total() {
      return this.sale.products.reduce((a, b) => a + (b && Number(b.total) || 0), 0);
    },

    valid() {
      return Object.values(this.validDate).every(el => el) && this.validForm && this.sale.products.length > 0
    },

    availableProducts() {
      return this.products.filter(el => !this.sale.products.map(prod => prod && prod.idProdotto).includes(el.idProdotto))
    }
  },

  methods:
  {
    calcTotal(item)
    {
      const quantity = Number(item.quantity);
      const cost = Number(item.costoUnitario);

      if(!quantity || !cost)
        item.total = 0;
      else
        item.total = Number((quantity * cost).toFixed(2));

      this.sale.products.splice();
    },

    removeManpower(index)
    {
      this.$delete(this.validDate, index)
      this.sale.products.splice(index, 1);
    },

    add()
    {
      this.dialog = true;
      this.sale = { products: [] };
    },

    async createPatient()
    {
      this.loadingPatient = true

      try
      {
        const response = await this.$axios.$post('/Pazienti/insert', this.patient);

        this.patients.push(response);
        this.sale.patient = response;
        this.$notifier.showMessage('Paziente creato con successo');

        this.loadingPatient = false;
      }
      catch (e)
      {
        this.$nuxt.error(e)
      }
    },

    async create()
    {
      this.loading = true

      try
      {
        await this.$axios.$post('Acquisti/newAcquisto', {
          DataAcquisto : new Date(),
          paziente: { idPazienti: this.sale.patient.idPazienti },
          Totale: this.total,
          prodotti: this.sale.products.map(el => ({
            quantita: el.quantity,
            Codice_Regionale_Medico: el.regionalCode,
            DataRicetta: el.date,
            Prodotto: { idProdotto: el.idProdotto }
          }))
        });

        this.purchases = await this.$axios.$get('/Acquisti/getAcquisti');

        this.$notifier.showInfo('Vendita creata con successo');
        this.loading = this.dialog = false;
      }
      catch (e)
      {
        console.error(e);
        // this.$nuxt.error(e)
      }
    }
  }
}
</script>
