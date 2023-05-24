<script setup>
import { onMounted, ref, defineProps } from "vue";
import axios from "axios";

const props = defineProps({
  userId: {
    type: [Number, String],
    required: true
  },
});

const userId = ref();

onMounted(() => {
  console.log(props.userId)
  axios.get(`/api/test/user/${props.userId}`, {})
      .then((response) => {
        console.log(response)
        userId.value = response.data;
      }).catch((error) => {
        if (error.response) {
          alert(error);
        }
      });
});
</script>

<template>
  <!-- 내용 생략 -->
  <h1>UserDetail</h1>
</template>

<style scoped>
/* 내용 생략 */
</style>
