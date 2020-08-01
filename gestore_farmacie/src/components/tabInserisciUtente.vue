<template id = "tabVendita">
  <b-container>
    <b-row >
      <b-col lg="4">
        <v-form>
          <v-text-field
            label="Nome"
            v-model="newUser.FirstName"
            required
            :rules="[v => !!v || 'Inserisci Nome']"
            @input = "onChangeNewUser()"
          ></v-text-field>
          <v-text-field
            label="Cognome"
            v-model="newUser.LastName"
            type="text"
            :rules="[v => !!v || 'Inserisci Cognome']"
            @input = "onChangeNewUser()"
          ></v-text-field>
          <v-text-field
            label="UserName"
            v-model="newUser.UserName"
            type="text"
            :rules="[v => !!v || 'Inserisci Username']"
            @input = "onChangeNewUser()"
          ></v-text-field>
          <v-text-field
          label="Email"
          v-model="newUser.Email"
          :rules= "[v => checkMail(v) || 'Inserisci Email']"
          type="text"
          @input = "onChangeNewUser()"
          >
          </v-text-field>
          <v-text-field
            label="password"
            v-model="newUser.Password"
            type="password"
            required
            :rules="[v => checkPassword(v) || 'Inserisci una Password']"
            @input = "onChangeNewUser()"
          ></v-text-field>
          <v-select
            label="Profilo"
            v-model= "newUser.Profilo"
            filled
            :items="Profiles"
            @change = "onChangeNewUser()"
          ></v-select>
        </v-form>

        <v-spacer></v-spacer>
        <v-btn color="primary" @click= "addUser()" :disabled = "addDisabled" >Aggiungi Utente</v-btn>
       </b-col>

      <b-col lg="8">
        <v-data-table
          :headers="fields"
          :items="users"
          :items-per-page="10"
          class="elevation-1"
        >
            <template v-slot:item="row">
              <tr>
                <td>{{row.item.FirstName}}</td>
                <td>{{row.item.LastName}}</td>
                <td>{{row.item.UserName}}</td>
                <td>{{row.item.Email}}</td>
                <td>{{row.item.Profilo}}</td>
                <td>
                    <v-btn class="mx-2" fab dark small color="red" @click="onDeleteButton(row.item)">
                        <v-icon dark>delete</v-icon>
                    </v-btn>
                    <v-btn class="mx-2" fab dark small color="yellow" @click="onUpdateButton(row.item)" v-b-modal.editGameModal>
                        <v-icon dark>edit</v-icon>
                    </v-btn>
                </td>
              </tr>
          </template>
        </v-data-table>
      </b-col>
    </b-row>
    <b-modal id="editGameModal" title="Modifica Gioco" @ok="onUpdate()">
      <v-text-field
            label="Nome"
            v-model="editUser.FirstName"
            required
            :rules="[v => !!v || 'Inserisci Email']"
          ></v-text-field>
          <v-text-field
            label="Cognome"
            v-model="editUser.LastName"
            type="text"
          ></v-text-field>
          <v-text-field
            label="UserName"
            v-model="editUser.UserName"
            type="text"
          ></v-text-field>
          <v-text-field
          label="Email"
          v-model="editUser.Email"
          type="text"
          >
          </v-text-field>
          <v-select
            label="Profilo"
            v-model="editUser.Profilo"
            :items="Profiles"
            filled
          >
          </v-select>
      <!-- <b-form-group label="Titolo" label-for="titolo">
        <b-form-input type="text" name="titolo" v-model="editGame.Titolo" />
      </b-form-group>

      <b-form-group label="Software House" label-for="softwareHouse">
        <b-form-input type="text" name="softwareHouse" v-model="editGame.SoftwareHouse" />
      </b-form-group> -->
    </b-modal> 
  </b-container>
</template>

<script>
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Api from '../services/api.js'
export default {
  methods:{
    onChangeNewUser(){
      if(this.checkPassword(this.newUser.Password) && this.newUser.FirstName  && this.newUser.LastName && this.newUser.UserName && this.newUser.Email && this.newUser.Profilo)
        this.addDisabled= false;
      else 
        this.addDisabled= true;
    },
    checkMail(email){
     
      if(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email))
        return true
      return "Formato email errato"
    },
    checkPassword(pass){
      if (pass.length < 8) {
        return("Minimo 8 caratteri");
      } else if (pass.length > 50) {
        return("Massimo 50 caratteri");
      } else if (pass.search(/\d/) == -1) {
        return("Inserire almeno un numero");
      } else if (pass.search(/[a-zA-Z]/) == -1) {
        return("Inserire almeno una lettera");
      } else if (pass.search(/[^a-zA-Z0-9\!\@\#\$\%\^\&\*\(\)\_\+]/) != -1) {
        return("Simbolo non supportato");
    }
    return true;
    },
    onDeleteButton(row){
      var self = this;
      API().delete("/deleteUserById",{
        id: this.originKey
      }).then((response)=>{
        if(response.data.success){
          self.getUsers();
        }
      })
    },
    onUpdateButton(row) {
      this.originKey = row.id;
      this.editUser = {
          FirstName: row.FirstName,
          LastName: row.LastName,
          UserName: row.UserName,
          Password: row.Password,
          Email: row.Email,
          Profilo: row.Profilo,
          id: row.id
      } 
    },
    onUpdate(){
      var self = this;
      Api().post("/updateUserById",{
        id: self.originKey,
        user : self.editUser
      }).then((response)=>{
        if(response.data.success){
          self.getUsers();
        }
      })
    },
    getUsers(){
      Api().get("/getUsers").then((response)=>{
            if(response.data.users){
              self.users = response.data.users;
            }
          })
    },
    addUser(){
      var self = this;
      Api().post("/addUser", {
        user: self.newUser
      }).then((response)=>{
        if(response.data.success){
          self.getUsers();
          self.newUser= {
            FirstName: "",
            LastName: "",
            UserName: "",
            Password: "",
            Email: "",
            Profilo: ""
            }
          }
      })
    }
  },

  data() {
    return {
      addDisabled: true,
      Profiles:[
        "Titolare Farmacista","Operatore di Banco","Dottore Farmacista"
      ],
      originKey: "",
      editUser: {},
      newUser: 
        {
          FirstName: "",
          LastName: "",
          UserName: "",
          Password: "",
          Email: "",
          Profilo: "",
          id:""
        }
      ,
      users:[
          {
          id: "1",
          FirstName: "Davide",
          LastName: "D'Angelo",
          UserName: "DaDan",
          Password: "",
          Email: "davide.dangelo9@gmail.com",
          Profilo: "Titolare Farmacista"
          }
      ],
      editGame: {},
      submitIsDisabled: true,
      fields: [
        {
          value: "FirstName",
          align: 'start',
          text: "Nome",
          sortable: true
        },
        {
          value: "LastName",
          text: "Cognome",
          sortable: true
        },
        {
          value: "UserName",
          text: "Username",
          sortable: false
          //   formatter: (value, key, item)=>{
          //     return this.getGameType(value)
          //   }
        },
        {
            value: "Email",
            text: "Email",
            sortable: true
        },
        {
          value: "Profilo",
          text: "Profilo",
          sortable: true
          
          //   formatter: (value, key, item)=>{
          //     return this.getPiattaforma(value)
          //   }
        },
        {
          value: "azioni",
          text: "",
          sortable: false
        }
      ]
    };
  }
};
</script>


<style lang="css">
  #tabVendita {
    background-color : black
  }

</style>