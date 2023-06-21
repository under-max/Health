<template>
  <div class="detailContainer" v-if="centerDetail">    
    <div class="centerDetailDiv">

      <div class="imgBox">        
        <span :class="slideIndex === 0 ? 'leftBtnEnd' : 'leftBtn'">
          <i class="fas fa-angle-left fa-2x" @click="moveLeftImage"></i>
        </span>
        <div class="imageContainer">            
          <img :src="getImage(slideIndex)" alt="" />
        </div>        
        <span :class="slideIndex === centerDetail.fileName.length-1 ? 'rightBtnEnd' : 'rightBtn'">
          <i class="fas fa-angle-right fa-2x" @click="moveRightImage"></i>
        </span>
      </div>

      <div class="centerDetailTxt">        
        <h2>센터명: {{ centerDetail.name }}</h2>
        <h2>주소 : {{ centerDetail.address }}</h2>
        <h2>정보 : {{ centerDetail.info }}</h2>
        <h2>전화번호: {{ centerDetail.phoneNumber }}</h2>
        <hr>

        <div v-if="centerDetail.trainerName.length !== 0">
           <h5>트레이너 명단</h5>

           <div class="trainerName">
               <h5 v-for="(data, index) in centerDetail.trainerName" 
               :key="index" 
               @click="getTrainerInfo(centerDetail.trainerId[index])"               
               >
                {{ data }}                
            </h5>
           </div>

        </div>  

        <template v-else>
            <h4>저장된 트레이너 정보가 없습니다.</h4>
        </template>
      </div>

    </div>
    
    <div class="CenterCommentContainer">
      <input type="text" v-model="centerCommentInfo" :placeholder="isLogin ? '' : '로그인 후 작성할 수 있습니다'" :disabled="!isLogin" @focus="messageCheck">
      

      <button @click="centerCommentSubmit" 
      :disabled="!(centerCommentCheck && centerCommentStarCheck && isLogin)"
      :class="centerCommentCheck && centerCommentStarCheck && isLogin ? 'insertCenterCommentActive' : 'insertCenterCommentDisabled'"
      >등 록</button>
      <div class="centerStarBox" v-if="isLogin">
        <p>{{!focusCheck ? '' : centerCommentInfo ? "" : "1글자 이상 입력하셔야 합니다."}}</p>
        <span><h4 class="star_h">평점:</h4></span>
        <span v-for="(starClass, index) in starsCopy" :key="index" :class="starClass" @click="isLogin && changeStars(index)"></span>
        <p>{{currentStarValue === 0 ? "평점을 입력해야 합니다" : ""}}</p>
      </div>

    </div>

    <div class="CenterComment" v-if="centerCommentData">
      <template v-for="(centerComment,index) in centerCommentData" :key="index">
        <CenterComment :centerComment="centerComment" :user="user" @changeReloadCenterComment="changeReloadCenterComment"></CenterComment>
      </template>
    </div>

    <div class="centerCommentAddBtnDiv">
      <button @click="checkComment" class="centerCommentAddBtn">더 보기</button>
    </div>

    <div v-if="trainerClose">
        <TrainerDetail :trainerDetail="trainerDetail" :trainerClose="trainerClose" @closeTrainerDetailModal="closeTrainerDetailModal" :reloadCenterComment="reloadCenterComment"></TrainerDetail>
    </div>

  </div>
</template>

<script setup>
import axios from "axios";
import Cookies from "vue-cookies";
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import TrainerDetail from "./TrainerDetail.vue";
import CenterComment from "./CenterComment.vue";
import { debounce } from "lodash";

import {showCustomAlert} from "../ui/Toast";
// url
const url = "http://localhost:8090/";

const route = useRoute();


//check
const focusCheck = ref(false);
const messageCheck = () => {
  focusCheck.value = true;
}
//reload centerComment
const reloadCenterComment = ref(false);

const changeReloadCenterComment = () => {
  reloadCenterComment.value = !reloadCenterComment.value;
  if(reloadCenterComment.value === true){
    centerCommentData.value = [];
    centerCommentDataLength.value = -1;
    centerCommentOffset.value = 0;
    getCenterComment();
  }
}



//trainer close 
const trainerClose = ref(false);
//트레이너 정보 닫기
const closeTrainerDetailModal = () => {
  trainerClose.value = false;
}

//경로매개변수 확인
const id = route.params.id;

//로그인 페이지로
const loginRedirect = () => {
    route.push("/login");
    console.log("hey");
}

//detail 저장 변수
const centerDetail = ref(null);

//trainer 저장 변수
const trainerDetail = ref(null);

//user 확인
const user = ref(null);
const centerToken = Cookies.get("accessToken")
const tokenCheck = async() => {  
  try{
    const response = await axios.get(url+"center/checkToken",{
      headers:{
        Authorization: centerToken,
      }
    })
    user.value = response.data;
  } catch(error){
    console.log(error);
  }
}
//Admin check
const userCheck = ref(false);

onMounted(()=>{
  if(centerToken !== null){
    tokenCheck();
    isLogin.value = true;    
  }
})

onMounted(async () => {
  try {
    const response = await axios.get(url + "center/centerDetail", {
      params: {
        centerId: id,
      },
    });
    centerDetail.value = response.data;
  } catch (error) {
    console.log(error);
  }
});


//이미지 슬라이드 처리
const slideIndex = ref(0);
const getImage = (index) =>{
    const fileName = centerDetail.value.fileName[index];
    return `https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerDetail.value.id}/${fileName}`
}

const moveLeftImage = () => {    
    if(slideIndex.value > 0 ){
        slideIndex.value = slideIndex.value - 1;        
    } else {
        slideIndex.value = 0;        
    } 
}

const moveRightImage = () => {    
    if(slideIndex.value < centerDetail.value.fileName.length -1){
        slideIndex.value = slideIndex.value + 1;        
    } else{        
        slideIndex.value = +(centerDetail.value.fileName.length -1);        
    }
}

const getTrainerInfo = async(trainerId) => {    
    try{
        const response = await axios.get(url+"center/getTrainerDetail",{
            params:{
                trainerId: trainerId,
            }
        })
    trainerDetail.value = response.data;  
    trainerClose.value = true
    }catch(error){
        console.log(error);
    }
}


//별점처리
const initialStar = ref([true,true,true,true,true]);
const currentStarValue = ref(0);
const stars = ref([]);
stars.value = initialStar.value.map((value)=> value ? 'fa-regular fa-star fa-2x star' : 'fa-solid fa-star fa-2x star');
const starsCopy = ref([]);
starsCopy.value = initialStar.value.map((value)=> value ? 'fa-regular fa-star fa-2x star' : 'fa-solid fa-star fa-2x star');



const changeStars = (index) => {  
  stars.value = initialStar.value.map((value)=> value ? 'fa-regular fa-star fa-2x star' : 'fa-solid fa-star fa-2x star');

  if(index === 0){
    initialStar.value[index] = !initialStar.value[index];
    stars.value[index] = initialStar.value[index] ? "fa-regular fa-star fa-2x star" : "fa-solid fa-star fa-2x star";
  } else{
    for(let i = 0; i <= index; i++){
      initialStar.value[i] = !initialStar.value[i];
      stars.value[i] = initialStar.value[i] ? "fa-regular fa-star fa-2x star" : "fa-solid fa-star fa-2x star";
    }
  }

  starsCopy.value = [];
  for(let i = 0; i < 5; i++){    
    starsCopy.value.push(initialStar.value[i] ? "fa-regular fa-star fa-2x star" : "fa-solid fa-star fa-2x star")
  }
  initialStar.value = [true,true,true,true,true];

  currentStarValue.value = index + 1;
};

//댓글 가져오기 
const centerCommentData = ref([]);
const getCenterCommentByCenterIdData = ref(0);
const centerCommentDataLength = ref(-1);
const centerCommentOffset = ref(0);

const getCenterComment = async() => {
  
  getCenterCommentByCenterIdData.value = id;
  if(centerCommentDataLength.value !== 0){
    try{
      const response = await axios.get(url+"center/getCenterComment",{
        params:{
          centerId: getCenterCommentByCenterIdData.value,
          offset: centerCommentOffset.value,
        }
      });
        centerCommentData.value = [...centerCommentData.value,...response.data.centerCommentData];
      if(centerCommentDataLength.value === -1){
        centerCommentDataLength.value = Math.floor(response.data.centerCommentCount / 5) +1;
      }
      centerCommentOffset.value = centerCommentOffset.value + 5;
      centerCommentDataLength.value = centerCommentDataLength.value - 1;
    }catch(error){
      console.log(error);
    }
  }else{
    showCustomAlert("마지막 페이지 입니다.");

  }
}

onMounted(()=>{  
  getCenterComment();
})

const checkComment = (e) => {
  e.preventDefault();
  getCenterComment();
}

//댓글 필요값 확인처리
const centerCommentCheck = ref(false);
const centerCommentStarCheck = ref(false);
const isLogin = ref(false);

//댓글 등록
const centerCommentInfo = ref('');
const centerCommentSubmitData = ref(null);

watch(centerCommentInfo, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    centerCommentCheck.value = true;    
  }else{
    centerCommentCheck.value = false;
  }
}, 200));

watch(currentStarValue,debounce((current, old)=>{
  if(current !== 0){
    centerCommentStarCheck.value = true;
  }else{
    centerCommentStarCheck.value = false
  }
}, 200))


const centerCommentSubmit = async(e) => {
  e.preventDefault();
  centerCommentSubmitData.value = {  
    star: currentStarValue.value,
    comment: centerCommentInfo.value,
    memberId: user.value.id,
    centerId: id, 
  }

  try{
    await axios.post(url+"center/insertCenterComment",centerCommentSubmitData.value)
    centerCommentSubmitData.value = null;
    currentStarValue.value = 0;
    centerCommentInfo.value = '';

    starsCopy.value = [];
    initialStar.value = [true,true,true,true,true];
    for(let i = 0; i < 5; i++){    
      starsCopy.value.push(initialStar.value[i] ? "fa-regular fa-star fa-2x star" : "fa-solid fa-star fa-2x star")
    }
    centerCommentData.value = [];
    centerCommentDataLength.value = -1;
    centerCommentOffset.value = 0;
    getCenterComment();
  }catch(error){
    console.log(error);
  }
}


</script>

<style scoped>
.detailContainer {
  width: auto;
  height: auto;
  background: white;
  min-height: 90vh;
  min-width: 100vw;
}

h1 {
  color: black;
}
.centerDetailDiv {
  display: flex;
  height: auto;
  min-width: 680px;
  min-height: 50vh;
  background: white;
  align-items: center;
  text-align: center;
  border-radius: 20px;
  margin-left: 3vh;
  margin-right: 3vh;
}


.centerDetailTxt {   
  width: 50%;
  height: auto;
}


.imgBox {
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    width: 50%;
    height: auto;
    margin-left: 3vh;
    object-fit: cover;
}
.imgBox img{
    width: 100%;   
    height: 400px;
    min-width: 100%;
    min-height: 100%;
}

.trainerName {
    display: flex;
    justify-content: center;
  }

  .trainerName h5 {
    color: #333;
    background: #f5f5f5;
    border-radius: 20px;
    padding: 10px 20px;
    margin: 5px;
    cursor: pointer;
    transition: background 0.3s;
  }

  .trainerName h5:hover {
    color: white;
    background: #2196f3;
  }


.CenterCommentContainer{
  margin-top: 1vh;
  /* margin-bottom: 1vh; */
  margin-left: 5vh;
  margin-right: 5vh;
  width: auto;
  min-width: 680px;
  height: auto;
  background: white;
  border-radius: 20px;
  display: flex;
  flex-wrap: wrap;
}

.CenterCommentContainer input{
  width: calc(100% - 150px);
  min-height: 102px;
  border-radius: 20px;
  padding: 0;
  border: 1px solid gray;
  font-size: 1.3rem;
}

.CenterCommentContainer button{  
  min-height: 100px;
  width: 150px;
  border-radius: 20px;
  
  color: white;
  font-size: 1.3rem;  
  border: none;
  padding: 0;   
}

.insertCenterCommentActive{
  background-color: #2196f3;
}
.insertCenterCommentDisabled{
  background-color: #ccc;
  cursor: not-allowed;
}

.star{
  color: rgb(245, 245, 0);
}

.blankStar{
  color: rgb(245, 245, 0)
}

.star_h{
  display: inline;
}

.CenterComment{
  margin-left: 5vh;
  margin-right: 5vh;
}

.centerCommentAddBtn{
  background-color: #2196f3;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
 
}

.centerCommentAddBtnDiv{
  margin-top: 2vh;
  background: white;
  display: flex;
  justify-content: center;
}
.centerCommentAddBtn:hover {
  background-color: #1976d2;
}


.centerDetailTxt h2 {
    font-size: 20px;
    color: #333;
    margin-bottom: 10px;
  }

  /* 트레이너 명단 스타일링 */
  .centerDetailTxt .trainerName {
    display: flex;
    gap: 10px;
  }

  .centerDetailTxt h4 {
    cursor: pointer;
    padding: 5px;
    background-color: #f5f5f5;
    border-radius: 4px;
    transition: background-color 0.3s;
  }

.centerStarBox p{
  color: red;
}

.leftBtn i{
  cursor: pointer;
}

.leftBtnEnd i {
  color: #E6E6FA;
  cursor: pointer;
}

.rightBtn i{
  cursor: pointer;
}

.rightBtnEnd i{
  color: #E6E6FA;
  cursor: pointer;
}
</style>