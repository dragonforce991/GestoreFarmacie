<template>
  <v-container fluid>
    <v-layout align-center justify-center row>
      <v-flex xs12 class="text-center">
        <img src="~/static/security.svg" width="400" height="300">
      </v-flex>

      <v-flex xs12 class="text-center">
        <h1 class="display-2 green--text">Whoops...</h1>
      </v-flex>

      <v-flex xs12 class="text-center">
        <p class="mt-4 headline">Pare che non hai le autorizzazioni necessarie per accedere a questa pagina</p>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  props: ['error']
}
</script>