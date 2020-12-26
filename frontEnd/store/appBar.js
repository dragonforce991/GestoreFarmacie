export const state = () => ({
  selectedRows: 0,
  visible: true,

  buttons: {
    addButton: true,
    deleteButton: true,
    cloneButton: true,
    excelButton: true,
    printerButton: true,
    importButton: true,
    pencilButton: true,
  }
})

export const mutations =
  {
    setSelectedRows(state, val) {
      state.selectedRows = val;
    },

    setButtons(state, val) {
      state.buttons = val;
    },

    setVisible(state, val) {
      state.visible = val
    },

    pencilClicked(state, val) {},
    importClicked(state, val) {},
    addClicked(state, val) {},
    deleteClicked(state, val) {},
    cloneClicked(state, val) {},
    excelClicked(state, val) {},
    printerClicked(state, val) {},
  };
