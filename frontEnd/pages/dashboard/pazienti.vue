<template>
  <v-container>
    <v-advanced-table dense outlined :columns="headers" v-model="patients" />

    <v-dialog v-model="dialog" :persistent="loading" width="700">
      <v-card>
        <v-toolbar color="primary white--text" class="elevation-0">
          <v-btn color="white" @click="dialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuovo Paziente</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!validForm" :loading="loading" @click="create">Crea</v-btn>
        </v-toolbar>

        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-col cols="12">
                <v-text-field
                  v-mask="'AAAAAA##A##A###A'"
                  :rules="$rules.fiscalCodeRule"
                  dense outlined label="Codice Fiscale"
                  v-model="patient.Codice_Fiscale"
                  counter="16"
                />
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Nome" v-model="patient.Nome"></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Cognome" v-model="patient.Cognome"></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-text-field :rules="$rules.basicRules" dense outlined label="Telefono" v-model="patient.Telefono"></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-advanced-date-picker v-model="patient.DataDiNascita"></v-advanced-date-picker>
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

  async asyncData({ $axios })
  {
    const patients = await $axios.$get('/Pazienti/getPazienti');
  console.log(patients);
    return {

      headers:
      [
        {
          text: 'Codice Fiscale',
          value: 'codice_Fiscale',
          dataType: 'text',
          caseSensitiveSelector: true,
        },

        {
          text: 'Nome',
          value: 'nome',
          dataType: 'text',
          caseSensitiveSelector: true
        },

        {
          text: 'Cognome',
          value: 'cognome',
          dataType: 'text',
          caseSensitiveSelector: true
        },
        {
          text: 'Data di Nascita',
          value: 'dataDiNascita',
          dataType: 'date',
        },
        {
          text: 'Telefono',
          value: 'telefono',
          dataType: 'text',
        }
      ],

      patients,
    }
  },

  data()
  {
    return {
      patient: {},

      loading: false, dialog: false, validForm: false
    }
  },

  mounted()
  {
    this.$store.commit('appBar/setVisible', true);
    this.$store.commit('appBar/setButtons', {
      addButton: true,
      deleteButton: false,
      cloneButton: false,
      excelButton: false,
      printerButton: false,
    });

    this.focusSearchUnsubscriber = this.$store.subscribe((mutation) =>
    {
      switch (mutation.type)
      {
        case 'appBar/addClicked': this.add();	break;
      }
    });
  },

  methods:
  {
    add()
    {
      this.dialog = true;
      this.patient = {};
    },

    async create()
    {
      this.loading = true

      try
      {
        
        const response = await this.$axios.$post('/Pazienti/insert', this.patient);
        console.log(this.patient);
        this.patients.push(response);
        this.$notifier.showInfo('Paziente creato con successo');

        this.loading = false;
        this.dialog = false;
      }
      catch (e)
      {
        console.log(e);
        //this.$nuxt.error(e)
      }
    }
  }
};
</script>
