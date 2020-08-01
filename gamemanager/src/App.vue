<template>
  <b-container>
    <div class="page-header">
      <h2 class="text-center">Game Manager</h2>
      <hr />
    </div>
    <b-row>
      <b-col lg="4">
        <b-form v-on:submit.prevent="onSubmit()">
        <b-form-group label = "Titolo" label-for= "titolo">
            <b-form-input type="text" name="titolo" v-model="newGame.Titolo" v-on:change="onChange()" />
        </b-form-group>

        <b-form-group label = "Software House" label-for= "softwareHouse">
            <b-form-input type="text" name="softwareHouse" v-model="newGame.SoftwareHouse" v-on:change="onChange()" />
        </b-form-group>

        <b-form-group label="Tipo" label-for="tipo">
            <b-form-select id="tipo" name="tipo" v-model="newGame.Tipo" :options="gameTypes">
            </b-form-select>
        </b-form-group>
        <b-form-group label= "Piattaforma" label-for="piattaforma" >
          <b-form-select v-model="newGame.Piattaforma" :options="gamePlatforms" name = "piattaforma">
          </b-form-select>
        </b-form-group>
        
        <b-btn type="submit" variant="primary" :disabled="submitIsDisabled">Aggiungi</b-btn>
        
        </b-form>
      </b-col>

      <b-col lg="8">
        <b-table striped hover :items="games" :fields="fields">
            <template v-slot:cell(azioni)="data">
              <b-btn
                size="sm"
                variant="warning"
                style = "margin : 5px"
                @click = "onDelete(data.item)"
              >
                X &nbsp;
              </b-btn>
              <b-btn
                size="sm"
                variant="secondary"
                style = "margin : 5px"
                @click = "onEdit(data.item)"
                v-b-modal.editGameModal
              >
                M
              </b-btn>
            </template>
        </b-table>
      </b-col>
    </b-row>
    <b-modal id = "editGameModal" title= "Modifica Gioco" @ok="onUpdate()">
        <b-form-group label = "Titolo" label-for= "titolo">
            <b-form-input type="text" name="titolo" v-model="editGame.Titolo" />
        </b-form-group>

        <b-form-group label = "Software House" label-for= "softwareHouse">
            <b-form-input type="text" name="softwareHouse" v-model="editGame.SoftwareHouse" />
        </b-form-group>

        <b-form-group label="Tipo" label-for="tipo">
            <b-form-select id="tipo" name="tipo" v-model="editGame.Tipo" :options="gameTypes">
            </b-form-select>
        </b-form-group>
        <b-form-group label= "Piattaforma" label-for="piattaforma" >
          <b-form-select v-model="editGame.Piattaforma" :options="gamePlatforms" name = "piattaforma">
          </b-form-select>
        </b-form-group>
    </b-modal>


  </b-container>

</template>

<script>
import { gamesRef } from './firebase'
import { GameTypeEnum, PiattaformaEnum } from './models/game'



export default {
  firebase: {
    games: gamesRef.orderByChild('Titolo')
  },
  methods: {
    getPiattaforma(value) {
      return this.gamePlatforms[value].text;
    },
    getGameType(value) {
      return this.gameTypes[value].text;
    },
    onChange(){
      this.submitIsDisabled = this.newGame.Titolo === "" || this.newGame.SoftwareHouse === ""
    },
    onSubmit(){
      console.log(this.newGame);
      gamesRef.push(this.newGame);
      this.newGame.Titolo='';
      this.newGame.SoftwareHouse='';
      this.submitIsDisabled= true;
    },
    onEdit(game){
      this.originKey = game['.key'];
      console.log(game);
      this.editGame = {
        Titolo: game.Titolo,
        SoftwareHouse: game.SoftwareHouse,
        Tipo: game.Tipo,
        Piattaforma: game.Piattaforma
      };
    },
    onDelete(game){
       gamesRef.child(game['.key']).remove();
    },
    onUpdate(){
      gamesRef.child(this.originKey).update(this.editGame);
    }
  },
  data() {
    return {
      
      submitIsDisabled: true,
      gameTypes: GameTypeEnum.properties,
      gamePlatforms: PiattaformaEnum.properties,
      
      editGame:{},
      originKey:'',

      newGame: {
        Titolo:'',
        softwareHouse:'',
        Piattaforma:0,
        Tipo:0
      },

      fields:[
        {
          key:"Titolo",
          label: "Titolo",
          sortable: true
        },
        {
          key:"SoftwareHouse",
          label:"Software House",
          sortable: true
        },
        {
          key:"Tipo",
          label:"Tipo",
          formatter: (value, key, item)=>{
            return this.getGameType(value)
          }
        },
        {
          key:"Piattaforma",
          label:"Piattaforma",
          formatter: (value, key, item)=>{
            return this.getPiattaforma(value)
          }
        },
        {
          key: "azioni",
          label: ""
        }

      ]
    }
  },
  



}


</script>

  <style lang="scss">
  .page-header {
    background-color: #226622;
    color: #ffffff;
  }
  .page-header h2 {
    padding-top: 8px;
  }

</style>
