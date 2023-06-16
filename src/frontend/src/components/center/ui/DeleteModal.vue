<template>
  <div class="modal">
    <div class="modal-content">
      <div class="modal-content-text">
          <h5>정말 삭제하시겠습니까?</h5>
      </div>
      <div class="button-container">
        <button class="confirm-button" @click="deleteCenterComment">확인</button>
        <button class="close-button" @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref,defineEmits, defineProps } from 'vue';
const props = defineProps({});

const emitEvent = defineEmits(["deleteCenterCommentModalBtn", "centerCommentDeleteProps"]);

// const deleteCenterCommentBtn = defineEmits(["centerCommentDeleteBtnProps"]);

const closeModal = () => {
  emitEvent("deleteCenterCommentModalBtn");
}

const deleteCenterComment = () =>{
  emitEvent("centerCommentDeleteProps");
}
</script>
  
<style scoped>
h1{
    color:black;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9999;
}
.modal-content-text{
  text-align: center;
  margin-top: 10%;
}
.modal-content {
  max-width: 40%;
  min-height: 200px;
  max-height: 50%;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  overflow: auto;
  position: relative;
  border-radius: 20px;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  position: absolute;
  bottom: 20px;
  right: 20px;
}

.close-button {
  background-color: #2196f3;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.close-button:hover {
  background-color: #1976d2;
}

.confirm-button {
  background-color: #f44336;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-button:hover {
  background-color: #d32f2f;
}
</style>