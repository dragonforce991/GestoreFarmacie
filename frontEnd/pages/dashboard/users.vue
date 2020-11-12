<template>
  <v-container>
    <v-advanced-table :slots="['role']" ref="table" @selected="selected = $event" dense outlined :columns="headers" v-model="users" >
      <template v-slot:role="{item}">
        {{item.role.id  == "1" ? "Regione" : item.role.id == "2" ? "Titolare" : item.role.id == "3" ? "Operatore di banco" : "Dottore Farmacista"}}
      </template>
    </v-advanced-table>

    <v-dialog v-model="dialog" width="700" :persistent="loading">
      <v-card>
        <v-toolbar class="elevation-0 white--text" color="primary">
          <v-btn color="white" @click="dialog = false" icon><v-icon>mdi-close</v-icon></v-btn>
          <v-toolbar-title>Nuovo Utente</v-toolbar-title>

          <v-spacer></v-spacer>

          <v-btn color="white" text :disabled="!validForm" :loading="loading" @click="create">Crea</v-btn>
        </v-toolbar>
        <v-container>
          <v-form v-model="validForm">
            <v-row dense>
              <v-banner>User</v-banner>
              <v-col cols="12">
                <v-text-field :rules="$rules.emailRules" type="text" dense outlined label="Email" v-model="user.email"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Nome" v-model="user.name"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Cognome" v-model="user.surname"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Telefono" v-model="user.phone_number"></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field :rules="$rules.basicRules" type="password" dense outlined label="Password" v-model="user.password"></v-text-field>
              </v-col>
              <v-col cols="6" v-if="$store.state.user.role.id!='1'">
                <v-combobox
                  :rules="$rules.basicRules"
                  outlined dense label="Ruolo"
                  v-model="user.role" :items="roles"
                  item-text="label" item-value="value" />
              </v-col>
              <v-row v-if="$store.state.user.role.id=='1'">
              
                <v-banner>Farmacia</v-banner>
                <v-col cols="12">
                  <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Indirizzo" v-model="farmacia.Indirizzo"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Nome Farmacia" v-model="farmacia.Nome"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field :rules="$rules.basicRules" type="text" dense outlined label="Telefono" v-model="farmacia.Telefono"></v-text-field>
                </v-col>
              </v-row>
              
            </v-row>
          </v-form>
        </v-container>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default 
{

  async asyncData({ $axios, store  }){
    var headers=
      [
        {
          text: 'Email',
          value: 'email',
          dataType: 'text',
          caseSensitiveSelector: false,
        },
        {
          text: 'Nome',
          value: 'name',
          dataType: 'text',
          caseSensitiveSelector: true,
        },{
          text: 'Cognome',
          value: 'surname',
          dataType: 'text',
          caseSensitiveSelector: true,
        },
        {
          text: 'Telefono',
          value: 'phone_number',
          dataType: 'text',
          caseSensitiveSelector: true,
        },
        {
          text: 'Ruolo',
          value: 'role',
          computedValue: (item) => item.role.id  == "1" ? "Regione" : item.role.id == "2" ? "Titolare" : item.role.id == "3" ? "Operatore di banco" : "Dottore Farmacista",
          dataType: 'text',
          caseSensitiveSelector: true,
        }
        
      ]
    const users = await $axios.$get('/user/getUsers');
    const roles = [{value : "3", label: "Operatore di Banco"},{value : "4", label: "Dottore Farmacista"}]
    if(store.state.user.role.id == '1'){
      headers.push({
          text: 'farmacia',
          value: 'nomeFarmacia',
          dataType: 'text',
          groupBy : true,
          caseSensitiveSelector: true,
        })
    }
    
    return {
      
      headers,
      users,
      roles
    }
  },
  data()
  {
    return {
      selected : [],
      user : {},
      farmacia:{},
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
      addButton: true,
      deleteButton:false,
      cloneButton: false,
      excelButton: false,
      printerButton:false,
      importButton: false,
      pencilButton: false,
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
      //this.product = {};
    },

    async create()
    {
      this.loading = true

      try
      {
        var response;
        if(this.$store.state.user.role.id == "1"){
          console.log("qui");
          this.user.role = {id : "2"};
          response = await this.$axios.$post('Farmacia/insert', {user : this.user, farmacia: this.farmacia});
        }else{
          this.user.role.id  = this.user.role.value; 
          console.log(this.user);
          response = await this.$axios.$post('/user/insert', this.user);
        }
        this.users = await this.$axios.$get('/user/getUsers');
        console.log(this.users);
        this.loading = false;
        this.dialog = false;
      }
      catch (e)
      {
        console.error(e);
        this.$notifier.showError(e);
        // this.$nuxt.error(e)
      }
        this.loading = false;
        this.dialog = false;
    }
  },
  
  layout: 'dashboard'
}
</script>