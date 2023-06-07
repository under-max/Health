<template>

  <div>

    <div class="mb-3">
      <label for="" class="form-label">제목</label>
      <input type="text" class="form-control" :value="board.title" readonly/>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">본문</label>
      <textarea class="form-control" readonly rows="10">{{board.content}}</textarea>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">작성자</label>
      <input type="text" class="form-control" :value="board.writer" readonly/>
    </div>

    <div class="mb-3">
      <label for="" class="form-label">작성일시</label>
      <input type="text" readonly class="form-control" :value="board.inserted"/>
    </div>

  </div>

</template>

<script setup>

import {defineProps, onMounted, ref} from "vue";
import axios from "axios";

const props = defineProps({
  boardId: {
    type: [Number, String],
    required: true
  },
});

const board = ref({
  title: '',
  content: '',
  writer: '',
  inserted: ''
})

onMounted(() => {
  axios
      .get(`/api/community/${props.boardId}`, {})
      .then((response) => {
        console.log(response.data);
        board.value = response.data;
      })
      .catch((error) => {
        console.log(error)
      });
});
</script>

<style scoped>

</style>