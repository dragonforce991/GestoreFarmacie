<template>
  <v-footer color="primary" app flat fixed bottom height="64" inset>
    <v-scale-transition>
      <v-btn class="mr-3" key="add" color="white" icon @click="buttonClicked('addClicked')" v-if="addButton && $store.state.auth.create">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn class="mr-3" key="pencil" color="white" icon @click="buttonClicked('pencilClicked')" v-if="pencilButton && $store.state.auth.modify && selectedRows > 1">
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn class="mr-3" key="import" color="white" icon @click="buttonClicked('importClicked')" v-if="importButton && $store.state.auth.create">
        <v-icon>mdi-table-arrow-up</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn class="mr-3" key="excel" color="white" icon @click="buttonClicked('excelClicked')" v-if="excelButton && selectedRows > 0">
        <v-icon>mdi-table-arrow-down</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn class="mr-3" color="white" icon @click="buttonClicked('cloneClicked')" v-if="cloneButton && selectedRows === 1">
        <v-icon>mdi-content-copy</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn class="mr-3" key="print" color="white" icon @click="buttonClicked('printerClicked')" v-if="printerButton && selectedRows > 0">
        <v-icon>mdi-printer</v-icon>
      </v-btn>
    </v-scale-transition>

    <v-scale-transition>
      <v-btn key="delete" color="white" icon @click="buttonClicked('deleteClicked')" v-if="deleteButton && selectedRows > 0">
        <v-icon>mdi-delete</v-icon>
      </v-btn>
    </v-scale-transition>
  </v-footer>
</template>

<script>
export default
{
  computed:
    {
      selectedRows() {
        return this.$store.state.appBar.selectedRows;
      },
      pencilButton() {
        return this.$store.state.appBar.buttons.pencilButton;
      },
      importButton() {
        return this.$store.state.appBar.buttons.importButton;
      },
      addButton() {
        return this.$store.state.appBar.buttons.addButton;
      },
      deleteButton() {
        return this.$store.state.appBar.buttons.deleteButton;
      },
      cloneButton() {
        return this.$store.state.appBar.buttons.cloneButton;
      },
      excelButton() {
        return this.$store.state.appBar.buttons.excelButton;
      },
      printerButton() {
        return this.$store.state.appBar.buttons.printerButton;
      }
    },
  methods:
    {
      buttonClicked(event) {
        this.$store.commit(`appBar/${event}`, null)
      }
    }
}
</script>
