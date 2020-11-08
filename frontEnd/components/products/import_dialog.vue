<template>
  <v-dialog v-model="importDialog" :width="step > 1 ? 1000 : 500" :persistent="loading || areThereCustomersNotValid">
    <v-card>
      <v-card-title class="primary white--text">
        <v-btn class="mr-3" color="white" :disabled="loading" @click="importDialog = false" icon>
          <v-icon>mdi-close</v-icon>
        </v-btn>

        {{ areThereCustomersNotValid ? 'Prodotti non importati' : 'Importazione prodotti'}}
      </v-card-title>

      <v-form v-model="validForm">
        <v-container>
          <v-row dense v-if="step === 0 && !areThereCustomersNotValid">
            <v-col cols="12" v-if="error">
              <v-alert type="error">
                {{error}}
              </v-alert>
            </v-col>

            <v-col cols="12">
              <v-advanced-file-picker v-model="file" @formData="file.formData = $event" label="File Excel" fieldName="attachment" outlined dense></v-advanced-file-picker>
            </v-col>

            <v-col cols="12">
              <v-btn color="primary" block @click="uploadFile" :loading="loading" :disabled="!validForm">Avanti</v-btn>
            </v-col>
          </v-row>

          <v-row dense v-if="step === 1 && !areThereCustomersNotValid">
            <v-col cols="12">
              <v-select :rules="basicRules" label="Foglio" v-model="worksheet" :items="worksheets" item-text="name" return-object outlined dense></v-select>
            </v-col>

            <v-col cols="12">
              <v-btn color="primary" block @click="step++" :disabled="!validForm">Avanti</v-btn>
            </v-col>
          </v-row>

          <v-row dense v-if="step === 2 && !areThereCustomersNotValid">
            <v-col cols="6">
              <v-select clearable label="ID" v-model="columnMatches.idProdotto" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="6">
              <v-select clearable label="Qtà." v-model="columnMatches.quantita" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select clearable label="Obbligo Ricetta" v-model="columnMatches.ObbligoRicetta" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select label="Costo Unitario" v-model="columnMatches.CostoUnitario" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select label="Nome" v-model="columnMatches.Nome" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select clearable label="Descizione" v-model="columnMatches.Descizione" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select label="Codice" v-model="columnMatches.Codice" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="4">
              <v-select label="Azienda" v-model="columnMatches.Azienda" outlined dense :items="worksheet.columns"></v-select>
            </v-col>

            <v-col cols="12">
              <v-btn color="primary" block @click="uploadData" :loading="loading" :disabled="!validForm">Importa</v-btn>
            </v-col>
          </v-row>

          <v-row dense v-if="areThereCustomersNotValid">
            <v-col cols="12">
              <v-simple-table>
                <template v-slot:default>
                  <thead>
                  <tr>
                    <th class="text-left">Riga</th>
                    <th class="text-left">Motivo</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="item in customersNotImported" :key="item.index">
                    <td>{{ item.index + 2 }}</td>
                    <td>{{ item.reason }}</td>
                  </tr>
                  </tbody>
                </template>
              </v-simple-table>
            </v-col>

            <v-col cols="12">
              <v-btn color="primary" block @click="hide">Conferma</v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script>
import * as XLSX from 'xlsx';

export default
{
  data()
  {
    return {
      validForm: false, loading: false,

      file: null, step: 0, error: '',
      worksheets: [], worksheet: null,
      columnMatches: {},
      customersNotImported: null,
      importDialog: false,
    }
  },

  computed:
  {
    areThereCustomersNotValid()
    {
      if(!this.customersNotImported)
        return false;

      return this.customersNotImported.length > 0
    },
  },

  methods:
  {
    reset()
    {
      this.file = null;
      this.step = 0;
      this.error = '';
      this.worksheets = [];
      this.worksheet = null;
      this.columnMatches = {};
      this.customersNotImported = null;
    },

    show()
    {
      this.step = 0
      this.importDialog = true;
    },

    hide() {
      this.reset();
      this.importDialog = false;
    },

    async uploadData()
    {
      try
      {
        this.loading = true;

        const productsToAdd = [];
        const notImported = [];
        const productsToImport = { magazzino: [] };

        for (let i = 0; i < this.worksheet.data.length; i++)
        {
          const row = this.worksheet.data[i];

          if(!row[this.columnMatches.idProdotto])
          {
            productsToAdd.push({
              rowIndex: i,
              data: {
                "ObbligoRicetta": row[this.columnMatches.ObbligoRicetta],
                "CostoUnitario" : row[this.columnMatches.CostoUnitario],
                "Nome": row[this.columnMatches.Nome],
                "Azienda": row[this.columnMatches.Azienda],
                "Descizione": row[this.columnMatches.Descizione],
                "Codice": row[this.columnMatches.Codice],
                "ParoleChiave": []
              }
            })
          }
          else
          {
            productsToImport.magazzino.push({
              "idProdotto": row[this.columnMatches.idProdotto],
              "quantita": row[this.columnMatches.quantita]
            })
          }
        }

        const promises = [];

        productsToAdd.forEach(product =>
        {
          const promise = this.$axios.$post('Product/insertProduct', product.data)
            .then(res => productsToImport.magazzino.push({
              "idProdotto": res,
              "quantita": this.worksheet.data[product.rowIndex][this.columnMatches.quantita]
            }))
            .catch(() => notImported.push({ index: product.rowIndex, reason: 'Non è stato possibile creare questo nuovo prodotto' }))

          promises.push(promise)
        })

        if(promises.length > 0) // NB: non se questo contorllo sia in realtà utile
          await Promise.all(promises);

        if(productsToImport.magazzino.length > 0)
          await this.$axios.$post('Magazzino/insertMagazzino', productsToImport);

        if(notImported.length > 0)
          this.customersNotImported = notImported;
        else
          this.hide();

        this.$notifier.showInfo('Importazione completata');
      }
      catch (e)
      {
        // this.$nuxt.error(e)

        console.error(e);
      }
      finally
      {
        this.loading = false;
      }
    },

    async uploadFile()
    {
      this.loading = true;

      const reader = new FileReader();

      reader.onload = (evt) =>
      {
        const data = evt.target.result;
        const workbook = XLSX.read(data, {
          type: 'binary'
        });

        const worksheetsData = [];

        for (let i = 0; i < workbook.SheetNames.length; i++)
        {
          const sheetName = workbook.SheetNames[i];
          const sheet = workbook.Sheets[sheetName];
          const data = XLSX.utils.sheet_to_json(sheet);

          worksheetsData.push({
            id: i,
            name: sheetName,
            data,
            columns: data[0] ? Object.keys(data[0]) : []
          })
        }

        this.worksheets = worksheetsData;
        this.loading = false;

        if(this.worksheets.length === 1)
        {
          this.worksheet = this.worksheets[0];
          this.step += 2;
        }
        else if(this.worksheets.length > 1)
          this.step++;
        else
        {
          this.error = "Nessun foglio disponibile"
          this.file = null;
        }
      };

      reader.readAsBinaryString(this.file);
    }
  }
}
</script>
