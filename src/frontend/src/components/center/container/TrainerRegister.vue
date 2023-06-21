<template>
  <div class="trainerRegister">
    <div class="black-bg">
      <h4>트레이너 등록</h4>
      <div class="white-bg">
        <div class="searchCenterTableTrainer">
            <label for="center">센터검색: </label>
            <input type="text"
            id="center" 
            v-model="centerName"
            @keydown.enter="tr_centerSearch_btn" />
            <button
              @click="tr_centerSearch_btn"              
            >
            검색
            </button>
        </div>
        <hr>

        <div class="searchMemberTableTrainer">
            <label for="searchMemberForTrainer">회원검색: </label>
            <input
                type="text"
                id="searchMemberForTrainer"
                @keydown.enter="tr_memberSearch_btn"
                v-model="memberName"
            />
            <button @click="tr_memberSearch_btn">검색</button>
            <hr />
        </div>

        <form enctype="multipart/form-data">
          <!-- 결과표시 -->
          <div v-if="showData.center !== '' && showData.memberName !== ''" class="result-info">
            <h3>센터명: {{ showData.center }}</h3>
            <h3>이름 : {{ showData.memberName }}</h3>
            <h3>별명 : {{ showData.nickName }}</h3>
            <h3>이메일: {{ showData.email }}</h3>
            <br />
            <label for="trainer_input_info">트레이너 정보를 입력하세요</label
            ><br />
            <input
              type="text"
              id="trainer_input_info"
              v-model="trainerInfo"
            /><br />

            <label for="trainer_input_info">트레이너 상세정보를 입력하세요</label
            ><br />
            <textarea              
              id="trainer_input_info"
              v-model="trainerInfoDetail"
            /><br />


            <div class="file-input-container">
                <label for="trainer_pic" class="file-input-label">사진등록</label><br />
                <input
                  type="file"
                  id="trainer_pic"
                  accept="image/*"
                  @change="trainer_img"
                  class="file-input"
                />
                <p class="trainerImgInfo">
                    {{trainerImgInfo.length === 0 ? 
                    '' : 
                    trainerImgInfo.length+"개의 파일이 등록되었습니다."}}</p>
            </div>

            <br />
            <hr />
            <button type="submit" 
                    @click="trainerInfoSubmitBtn" 
                    :disabled="!(submitValueCheck.infoHaveData === true &&
                                submitValueCheck.infoDetailHaveData === true &&
                                trainerImgInfo.length === 1 && 
                                trainerImgInfo.name.length < 100)">
              등록하기
            </button>
          </div>
        </form>

        <hr />
    
        <div class="trainerModifyContainer">
            <button @click="modifyModal" class="trainerModify">수정하기</button>
        </div>
      
    </div>
      
    </div>
  </div>

      <!-- 검색 모달창 -->
      <div v-if="modalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModalBtn">&times;</span>
        <h3 class="modal-title">검색 결과</h3>
        <ul class="search-results">
          <li v-for="(data, index) in tr_Result" 
            :key="index" 
            class="search-result" 
            @click="searchChecker === 1 
            ? centerChooseBtn(data.id, data.name) 
            : trainerChooseBtn(data.id, data.email, data.name, data.nickName, data.authority)">
            {{data.name }}
            {{data.email ? "    이메일:("+data.email+")" : ''}}
        </li>
        </ul>
      </div>
    </div>




</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from "vue";
import { debounce } from "lodash";
import axios from "axios";
import {showCustomAlert} from "../ui/Toast"
import { useRouter } from "vue-router";


const router = useRouter();
//리스트로 리다이랙트
const centerRedirect = () => {
    router.push("/center")
}


//모달 확인중
const modalOpen = ref(false);

const closeModalBtn = () => {
    modalOpen.value = false;
}

const openModalBtn = () => {
    if(centerGetData.value.length !== 0){
        modalOpen.value = true;
    }
}

//url
const url = "http://localhost:8090/";

//Modal props 설정
const props = defineProps({
  isRegister: Boolean,
});

const emit = defineEmits(["isModify"]);

const modifyModal = () => {
  emit("isModify");
};

const centerChooseBtn = (id,name) => {
    showData.value.center = name;
    trainerData.value.centerId = id;
    centerName.value = name;
    closeModalBtn();
}
// 전체 값 모아서 화면 보여줄 변수 선언
const showData = ref({
  center: "",
  memberName: "",
  nickName: "",
  email: "",
});

// 찾아온 값 저장 추후 controller로 Data 보내는 변수 선언
const trainerData = ref({
  memberId: 0,
  centerId: 0,
  info: "",
  infoDetail:"",
  name: "",
  nickName: "",
  authority: 2,
});

//검색 구분자
const searchChecker = ref(0);


//CenterName Data 가져오기
const centerName = ref("");
const tr_Result = ref([]);
const tr_centerSearch_btn = async (e) => {
  e.preventDefault();  
  try {
    const response = await axios.get(url + "center/centerNameInfo", {
      params: {
        centerName: centerName.value,
      },
    });
    tr_Result.value = response.data;
    searchChecker.value = 1;
    modalOpen.value = true;
  } catch (error) {
    console.log(error);
  }
};


//등록한 멤버가 이미 해당 센터에 등록되어있는 트레이너 인가?

const checkTrainer = async(memberId, centerId) => {        
    try{
        const response = await axios.get(url+"center/checkTrainer",{
            params:{
                memberId,
                centerId
            }
        })
        if(response.data.checker === false){
            showCustomAlert(response.data.message)
        }else{
            showCustomAlert(response.data.message)
        }
     
    }catch(error){
        console.log(error);
    }
}

const trainerChooseBtn = (id,email,name,nickName, authority) => {
    memberName.value = name;   
    showData.value.memberName = name;
    showData.value.nickName = nickName;
    showData.value.email = email;
    
    trainerData.value.memberId = id;
    trainerData.value.name = name;
    trainerData.value.nickName = nickName;
    checkTrainer(trainerData.value.memberId, trainerData.value.centerId);

    closeModalBtn();
}
//Member검색용
const memberName = ref("");
//Member table에서 가져오기
const tr_memberSearch_btn = async (e) => {
  e.preventDefault();
  //값 받아오기 위한 초기화
  tr_Result.value = [];

  try {
    const response = await axios.get(url + "center/membersearch", {
      params: {
        memberName: memberName.value,
      },
    });
    tr_Result.value = response.data;
    searchChecker.value = 0;
    modalOpen.value = true;    
  } catch (error) {
    console.log("실패");
  }
};

//필요한 정보 다 입력했는지 확인
const submitValueCheck = ref({
    infoHaveData: false,
    infoDetailHaveData: false,
})

const trainerInfo = ref('');
const trainerInfoDetail = ref('');
watch(trainerInfo, debounce((current, old)=>{
    if(current !== '' && current.trim() !== ''){
        submitValueCheck.value.infoHaveData = true;
    }
}, 200));

watch(trainerInfoDetail, debounce((current, old)=>{
    if(current !== '' && current.trim() !== ''){
        submitValueCheck.value.infoDetailHaveData = true; 
    }    
},200))


//트레이너 정보 create 로직

//파일 체크
const trainerImgInfo = ref({
    name: '',
    length: 0
});
const trainer_pic = ref([]);

const trainer_img = (e) => {
  const files = e.target.files;
  // trainer_pic.value.push(files);
  for (let i = 0; i < files.length; i++) {
    trainer_pic.value.push(files[i]);
  }  
  trainerImgInfo.value.name = files[0].name;
  trainerImgInfo.value.length = files.length;    
};

// Controller 송신
const trainerInfoSubmitBtn = async (e) => {
  e.preventDefault();

  trainerData.value.info = trainerInfo.value;
  trainerData.value.infoDetail = trainerInfoDetail.value;

  const trainerTotalData = new FormData();
  for (let i = 0; i < trainer_pic.value.length; i++) {
    trainerTotalData.append("trainerImg", trainer_pic.value[i]);
  }

  trainerTotalData.append("memberId", trainerData.value.memberId);
  trainerTotalData.append("centerId", trainerData.value.centerId);
  trainerTotalData.append("info", trainerData.value.info);
  trainerTotalData.append("infoDetail", trainerData.value.infoDetail);
  trainerTotalData.append("name", trainerData.value.name);
  trainerTotalData.append("nickName", trainerData.value.nickName);
  trainerTotalData.append("authority", trainerData.value.authority);

  try {
    await axios.post(url + "center/inputInfoTrainer", trainerTotalData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    centerRedirect();
  } catch (error) {
    console.log(error);
  }
};
</script>

<style scoped>
.trainerRegister h4 {
  color: black;
  text-align: center;
}

h3, label {
    color: black;
}
.black-bg {
  background-color: #f5f5f5;
  padding: 20px;
  color: #fff;
}

.white-bg {
  background-color: #fff;
  padding: 20px;
}


.searchCenterTableTrainer, .searchMemberTableTrainer {
  margin-bottom: 10px;
}

label {
  margin-right: 5px;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
  color: #333;
  transition: border-color 0.3s ease;
}

textarea{
    height: 120px;
}

input[type="text"]:focus,
textarea:focus {
  outline: none;
  border-color: #3498db;
}

input[type="file"] {
  display: none;
}

label {
  display: inline-block;
  margin-bottom: 5px;
  font-weight: bold;
}

button {
    width: 100%;
    display: inline-block;
    padding: 10px 20px;
    background-color: #2196f3;
    color: #fff;
    border: none;
    border-radius: 5px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

button:hover {
  background-color: #23527c;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}


.file-input-container {
  position: relative;
  display: inline-block;
  overflow: hidden;
  vertical-align: middle;
}

.file-input-label {
  display: inline-block;
  padding: 10px 20px;
  background-color: #2196f3;
  color: #fff;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.file-input {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.file-input-label:hover {
  background-color: #23527c;
}

.file-input-label:active {
  background-color: #2574a9;
}


.result-info {
  margin-bottom: 0;
}

.result-info h3 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 500;
  color: #333;
  margin-bottom: 3px;
}


.trainerModifyContainer {    
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.trainerModify {
    min-width: 100%;
    padding: 10px 20px;
    background-color: yellow;
    color: #333;
    border: none;
    border-radius: 5px;
    font-size: 14px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.trainerModify:hover {
  background-color: #ffed99;
}

.trainerModify:active {
  background-color: #ffd633;
}

.trainerImgInfo{
    color: black;
}


.normalImg{
    color: black;
}
.warringImg{
    color: red;
}






.modal {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  width: 400px;
  padding: 20px;
  border-radius: 10px;
  max-height: 80vh;
  overflow-y: auto;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  color: #999;
  cursor: pointer;
}

.modal-title {
  margin-bottom: 10px;
}

.search-results {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.search-result {
  margin-bottom: 5px;
}


</style>