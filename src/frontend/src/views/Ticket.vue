<template>
  <h1>이용권안내!</h1>

  이름:<input type="text" name="name"/> <br>
  나이:<input type="text" name="age"/> <br>
  취미:<input type="text" name="hobby"/> <br>
  <input type="submit" value="전송"/>

</template>

<script>
import axios from "axios";
import {reactive} from "vue";

export default {
  name: "Ticket",
  setup() {
    const state = reactive({
      items: []
    });
    axios.get("/api/items")
        .then((response) => {
          console.log(response)
          state.items = response.data;
        });
  }
}
</script>

<style>

</style>