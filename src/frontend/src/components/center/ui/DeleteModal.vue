<template>
  <div class="modal">
    <div class="modal-content">
      <div class="modal-content-text">
          <h5>정말 삭제하시겠습니까?</h5>
      </div>
      <div class="button-container">
        <button v-if="deleteTrainerModalChecker === 2" class="confirm-button" @click="deleteTrainerData">확인</button>
        <button v-else-if="deleteCenterModalProps === false" @click="deleteCenterProps" class="confirm-button">확인</button>
        <button v-else class="confirm-button" @click="deleteCenterComment">확인</button>
        
        
        <button v-if="deleteTrainerModalChecker === 2" class="close-button" @click="closeTrainerDeleteModal">닫기</button>
        <button v-else-if="deleteCenterModalProps === false"
          class="close-button" @click="centerDeleteModalBtn">
          닫기
        </button>
        <button v-else class="close-button" @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref,defineEmits, defineProps } from 'vue';
const props = defineProps({
  deleteTrainerModalChecker:Number,
  deleteCenterModalProps:Boolean,
});

const emitEvent = defineEmits(["deleteCenterCommentModalBtn", 
                                "centerCommentDeleteProps",
                                "deleteTrainerModalBtn",
                                "trainerDeleteProps",
                                "centerDeleteModalBtn",
                                "deleteCenterProps"]);


const closeModal = () => {
  emitEvent("deleteCenterCommentModalBtn");
}

const closeTrainerDeleteModal = () => {
  emitEvent("deleteTrainerModalBtn");
}

const deleteCenterComment = () =>{
  emitEvent("centerCommentDeleteProps");
}

const deleteTrainerData = () => {
  emitEvent("trainerDeleteProps");
}

const centerDeleteModalBtn = () => {
  emitEvent("centerDeleteModalBtn");
}

const deleteCenterProps = () => {
  emitEvent("deleteCenterProps")
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
  margin-top: 8%;
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