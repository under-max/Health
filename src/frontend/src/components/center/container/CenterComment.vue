<template>

  <DeleteModal v-if="deleteCenterCommentModal" @deleteCenterCommentModalBtn="deleteCenterCommentModalBtn" @centerCommentDeleteProps="centerCommentDeleteProps"></DeleteModal>
  
  <div class="centerCommentContainer">
    <div class="centerCommentDiv">

        <div class="centerCommentInput">
            <h6 v-if="!centerCommentModifyComment">{{centerComment.comment}}</h6>
            <input v-else type="text" v-model="centerCommentComment" class='centerCommentModify'/>
        </div>

        <div class="centerCommentInfo">
            
            <div class="CommentStarDiv">
                <i class='fa-solid fa-star star'></i>
                <h5 v-if="!centerCommentModifyStar">{{centerComment.star}}</h5>
                <select v-else name="" id="" v-model="centerCommentStar">
                    <option value="5" :selected="centerCommentStar">5</option>
                    <option value="4" :selected="centerCommentStar">4</option>
                    <option value="3" :selected="centerCommentStar">3</option>
                    <option value="2" :selected="centerCommentStar">2</option>
                    <option value="1" :selected="centerCommentStar">1</option>
                </select>
            </div>

            <div class="centerCommentNickNameDiv">
                <h5 class="centerCommentNickName">닉네임: {{centerComment.nickName}}</h5>
            </div>

            <div class="centerCommentInsertedDiv">
                <h5 class="centerCommentInserted">시간: {{centerComment.inserted}}</h5>
            </div>
            <div v-if="user !==null" class="centerCommentModifyDiv">
                <i class="fa-regular fa-file centerCommentModify" 
                v-if="user.id === centerComment.memberId" 
                @click="centerCommentModifyBtn"
                @mouseover="sameValueCheck"></i>
                <i class="fa-solid fa-trash-can centerCommentDelete" 
                v-if="user.id === centerComment.memberId" 
                @click="centerCommentDeleteBtn"></i>
            </div>
        </div>
    </div>
  </div>

</template>

<script setup>
import axios from 'axios';
import { defineProps, ref, defineEmits } from 'vue';
import {showCustomAlert} from "../ui/Toast";
import DeleteModal from "../ui/DeleteModal.vue";

//delete Modal
const deleteCenterCommentModal = ref(false);
const deleteCenterCommentModalBtn = () =>{
    deleteCenterCommentModal.value = false;
}


const props = defineProps({
    centerComment: Object,
    user: Object,
    reloadCenterComment:Boolean,
})

const emit = defineEmits(["changeReloadCenterComment"]);

const reloadProps = () => {
    emit("changeReloadCenterComment");
}


// url
const url = "http://localhost:8090/";

const centerCommentData = ref(props.centerComment);

const centerCommentDelete = async() =>{
       
    let centerCommentId = centerCommentData.value.id
    try{
        await axios.delete(url+"center/deleteCenterComment",{
            params:{
                id: centerCommentId,
            }
        });
        reloadProps();//true 처리
        reloadProps();//false 처리
        showCustomAlert("삭제가 완료되었습니다.");
    }catch(error){
        console.log(error);
    }
}

//props를 위한 deleteBtn 처리
const centerCommentDeleteProps = () => {
    centerCommentDelete();
}

const centerCommentDeleteBtn = () => {
    deleteCenterCommentModal.value = true;
}
//수정을 위한 axios 통신 변수 값
const centerCommentId = ref(centerCommentData.value.id);
const centerCommentStar = ref(centerCommentData.value.star);
const centerCommentComment = ref(centerCommentData.value.comment);
//star 수정 처리
const centerCommentModifyStar = ref(false);
//comment 수정처리
const centerCommentModifyComment = ref(false);
//댓글 수정 누르면 class 변경
const centerModifyClass = ref(false);

const centerCommentModifySend = ref(0); //send 대기상태

//같은 값인지? 
const isSameValue = ref(centerCommentComment.value === centerCommentData.value.comment && centerCommentStar.value === centerCommentData.value.star)
const sameValueCheck = ()=>{
    isSameValue.value = centerCommentComment.value === centerCommentData.value.comment && centerCommentStar.value === centerCommentData.value.star;
}

//댓글 수정
const centerCommentModifyBtn = async(e) => {
    e.preventDefault();    
    centerModifyClass.value = !centerModifyClass.value;
    
    if(centerCommentModifySend.value === 0){
        centerCommentModifySend.value = 1; //input 열린상태 2번 되면 axios 통신처리
        
        centerCommentModifyStar.value = true;
        centerCommentModifyComment.value = true;
    }

    if(centerCommentModifySend.value === 2){
        try{
            await axios.put(url+"center/modifyCenterComment",{                
                    id: centerCommentId.value,
                    comment: centerCommentComment.value,
                    star: centerCommentStar.value                
            });
            reloadProps();//true 처리
            reloadProps();//false 처리
            showCustomAlert("수정이 완료되었습니다.");
        }catch(error){
            console.log(error);        }

        centerCommentModifySend.value = 0;
        centerCommentModifyStar.value = false;
        centerCommentModifyComment.value = false;
    }else{
        centerCommentModifySend.value = 2;
    }    

}
</script>

<style scoped>
h1{
    color:black
}
.centerCommentContainer{
    margin-bottom: 1vh;
    border-radius: 20px;
    background: white;
}

.centerCommentDiv{
    display: flex;
    justify-content: space-between;
    padding-top: 1vh;
    padding-bottom: 1vh;
    border-bottom: 1px solid gray;

    align-items: center;
}

.centerComment{
    margin-left: 1vh;
    text-align: left;
}

.centerCommentInfo{
    display:flex;
    text-align: right;

    align-items: center;
}

.centerCommentNickName{
    padding-right: 1vh;
    border-right: 1px solid gray;
    border-left: 1px solid gray;
}

.centerCommentInserted{
    margin-right: 2vh;
}

.centerCommentModify{
    padding-top: 4px;
    margin-right: 1vh;
    color: blue;
}
.centerCommentDelete{
    padding-top: 4px;
    margin-right: 1vh;
    color: red;
}

.star{
    padding-bottom: 0.7vh;
    padding-right: 0.7vh;
    color: rgb(242, 242, 0);
}

.centerCommentInput{
    /* min-height: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 1vh; */
    flex: 1;
    display: flex;
    align-items: center;
    padding: 0 1vh;
    width: calc(100% - 470px);
}

.centerCommentInput input{
    width: 100%
}
.centerCommentInput h6{
    width: 100%
}
.CommentStarDiv{
    display: flex;
    align-items: center;
    margin-right: 1vh;
}
.centerCommentModify{
    width: auto;
    min-height: 100%;
    border-radius: 30px;
}


.centerCommentModify,.centerCommentDelete {
  font-size: 1.2rem;
  cursor: pointer;
}

.centerCommentModify:hover,.centerCommentDelete:hover {
  color: #ff0000;
}
.centerCommentModify:focus,.centerCommentDelete:focus {
  outline: none;
}

.centerCommentModify:hover i,.centerCommentDelete:hover i {
  transform: scale(1.2);
}

.centerCommentInput h6{
    margin-top: 0.68vh;
}

.centerCommentInfo{
    min-width: 470px;
    width: auto;
}
</style>