<template>
  <v-row
    justify="center"
    no-gutters
  >
    <v-col
      class="msg-wrapper"
    >
      <v-row
        no-gutters
        justify="space-between"
        class="msg"
        :class="{ owner }"
      >
        <v-col>
          <span class="font-weight-bold">{{ message.fullName }}</span>
          <p class="mb-0">
            {{ message.message }}
          </p>
        </v-col>
        <v-col cols="auto">
          <span class="msg__date ml-3">{{ message.createdAt }}</span>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
      default: () => {},
    },
    owner: {
      type: Boolean,
    },
  },
  created(){
    console.log(this.message);
  }
};
</script>

<style lang="scss" scoped>
.system {
  margin-bottom: 1rem;
  color: #fff;

  p {
    margin-bottom: 1rem;
  }
}

.msg-wrapper {
  display: flex;
  flex-direction: column;
}

.msg {
  padding: 1rem;
  width: 60%;
  margin: 0 1rem;
  box-shadow: 0 1px 0 0 rgba(50, 50, 50, 0.3);
  border-radius: 4px;
  background: #e0e0e0;
  color: #000;
  position: relative;
  word-break: break-all;
  margin-bottom: 1rem;

  &__date {
    text-decoration: underline;
  }
}

.owner {
  background: #1976d2;
  color: #000;
  align-self: flex-end;
}

@media (max-width: 400px) {
  .msg {
    width: 90%;
  }
}
</style>
