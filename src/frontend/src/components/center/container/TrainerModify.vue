<template>  
    <DeleteModal v-if="deleteTrainerModal" 
                    :deleteTrainerModal="deleteTrainerModal" 
                    @deleteTrainerModalBtn="deleteTrainerModalBtn"
                    :deleteTrainerModalChecker="deleteTrainerModalChecker"
                    @trainerDeleteProps="trainerDeleteProps"/>
  <div class="black-bg">
      <h4>트레이너 수정하기</h4>
        <div class="white-bg">
        <div class="centerSearchContainer">
            <label for="modifySearch">센터검색</label>
            <input
              id="modifySearch"
              type="text"
              v-model="modifyTrainerName"
              @keydown.enter="modifyTrainerSearch"
            />
            <button @click="modifyTrainerSearch">검색</button>
        </div>


        <div v-if="haveFinalTrainerModifyData">
            <form action="" enctype="multipart/form-data">        
            <div class="modifyDataDetail">
                <h4>센터명:{{ ModifyTrainerData.centerName }}</h4>
                <h4>트레이너명:{{ ModifyTrainerData.name }}</h4>

            </div>
            <div class="trainerInfo">
                <label for="info">소개: </label>
                <input
                    id="info"
                    type="text"
                    v-model="infoCopy"                
                />
                <p>{{infoChecker ? "" : "최소 1글자이상 입력하세요"}}</p>
            </div>

            <div class="trainerInfoDetail"> 
                <label for="infoDetail">상세소개: </label>
                <textarea
                    id="infoDetail"
                    type="text"
                    v-model="infoDetailCopy"                
                />
                <p>{{infoDetailChecker ? "" : "최소 1글자이상 입력하세요"}}</p>
            </div>
            <div class="trainerImgContainer">
                <h4>트레이너 사진</h4>
                <div class="trainerCurrentImg">
                    <div class="inputCheckBox">
                      <div :class="fileName ? 'deleteCheckBtnRed' : 'deleteCheckBtnBlue'" @click="deleteCheckBtn">
                        <i class="fa-regular fa-trash-can">{{fileName ? '   삭제 예정인 이미지 입니다' : ''}}</i>
                      </div>

                    </div>
                    <img
                        :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${ModifyTrainerData.centerId}/${ModifyTrainerData.id}/${ModifyTrainerData.fileName}`"
                        alt="사진"
                    />
                </div>

                <div class="addTrainerImg">
                    <label for="addFile">사진등록</label>
                    <input
                        id="addFile"
                        type="file"
                        accept="image/*"
                        @change="trainerImage"
                    />
                    <p>{{imgChecker ? '' : '수정 후 1개 이상의 파일이 있어야 합니다 현재 최종 '}}{{+ !imgChecker? '0개' : ''}}</p>
                </div>
          
            </div>

            <hr />
        </form>
        <div class="TrainerModifyBtn">
            <button @click="TrainerModifySubmitBtn" :disabled="!(infoDetailChecker && infoChecker && imgChecker)">수정</button>
            <button class="trainerDeleteBtn" @click="deleteTrainerModal = !deleteTrainerModal">삭제</button>
        </div>
        </div>      


      <hr />
      <button @click="modifyModal">닫기</button>
    </div>
  </div>

  <div v-if="modalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModalBtn">&times;</span>
        <h3 class="modal-title">검색 결과</h3>
        
        <!-- 센터 검색의 경우 -->
        <ul class="search-results">
            <li v-for="(data, index) in centerSearchResult" 
                :key="index" 
                class="search-result"
                @click="selectCenterAndGetTrainer(data.name,data.id)">
                {{data.name}}
            </li>        
        </ul>
        <!-- 클릭한 센터 기준으로 trainer 검색 -->
        <ul class="search-results" v-if="TrainerResultData">
            <li v-for="(data, index) in TrainerResultData" 
                :key="index" 
                class="search-result"
                @click="selectTrainerAndDataSave(data.fileName, 
                                                            data.id, 
                                                            data.info, 
                                                            data.infoDetail,
                                                            data.memberId,
                                                            data.name)"
                >
                {{data.name}}
            </li>        
        </ul>

      </div>
    </div>
</template>



<script setup>
import { ref, defineEmits, defineProps, watch } from "vue";
import { debounce } from "lodash";
import axios from "axios";
import DeleteModal from "../ui/DeleteModal.vue";
import {showCustomAlert} from "../ui/Toast"
import { useRouter } from "vue-router";

//리스트로 리다이랙트
const router = useRouter();
const centerRedirect = () => {
    router.push("/center")
}

//Modal props 설정
const props = defineProps({
  isRegister: Boolean,
});

const emit = defineEmits(["isModify"]);

const modifyModal = () => {
  emit("isModify");
};

const modalOpen = ref(false);

const closeModalBtn = () => {
    modalOpen.value = false;
}

const trainerModifySearchType = ref(0);



//url
const url = "http://localhost:8090/";



// 센터검색
const modifyTrainerName = ref("");
// 센터이름검색결과
const centerSearchResult = ref([]);

// 센터 검색 get
const modifyTrainerSearch = async (e) => {
  e.preventDefault();
  try {
    const response = await axios.get(url + "center/searchCenterName", {
      params: {
        searchName: modifyTrainerName.value,
      },
    });
    centerSearchResult.value = response.data;
    trainerModifySearchType.value = 1;
    modalOpen.value = true;    

  } catch (error) {
    console.log(error);
  }
};

//수정 data 처리
const ModifyTrainerData = ref({
    centerId: 0,
    centerName: '',
    id: 0,
    info: '',
    infoDetail: '',
    memberId: '',
    name: '',
    fileName: ''
});

const selectCenterAndGetTrainer = (centerName, centerId)=>{    
    modalOpen.value = false;
    ModifyTrainerData.value.centerId = centerId;
    ModifyTrainerData.value.centerName = centerName;
    TrainerResultModifyData(centerName);    
}


//선택한 센터 id 저장
const TrainerModifyCenterName = ref('');

//axios로 받아올 데이터 저장
const TrainerResultData = ref();

// 센터이름 누르면 실행
const TrainerResultModifyData = async (centerName) => {    
    TrainerModifyCenterName.value = centerName;

  try {
    const response = await axios.get(url + "center/modifyTrainerSearch", {
      params: {
        centerName: TrainerModifyCenterName.value,
      },
    });
    TrainerResultData.value = response.data;
    modalOpen.value = true;
    centerSearchResult.value = null;
  } catch (error) {
    console.log(error);
  }
};

//최종 데이터 유무 확인
const haveFinalTrainerModifyData = ref(false);

const infoCopy = ref('');
const infoDetailCopy = ref('');
//트레이너 선택
const selectTrainerAndDataSave = (fileName, id, info, infoDetail, memberId, name) => {
    closeModalBtn();
    ModifyTrainerData.value.fileName = fileName;
    ModifyTrainerData.value.id = id;
    ModifyTrainerData.value.info = info;
    ModifyTrainerData.value.infoDetail = infoDetail;
    ModifyTrainerData.value.memberId = memberId;
    ModifyTrainerData.value.name = name;
    haveFinalTrainerModifyData.value = true;

    infoCopy.value = info;
    infoDetailCopy.value = infoDetail;
}


//삭제 Modal용
const deleteTrainerModalChecker = ref(2);
const deleteTrainerModal = ref(false);
const deleteTrainerModalBtn = () =>{     
    deleteTrainerModal.value = false;
}

// 트레이너 삭제 처리 props 
const trainerDeleteProps = () => {
    deleteTrainerModalBtn();
    TrainerDeleteSubmitBtn();
}
//트레이너 삭제 전송 버튼
const TrainerDeleteSubmitBtn = async () => { 
  try {
    await axios.delete(url + "center/deleteTrainer", {
      data: ModifyTrainerData.value,
    });
    showCustomAlert("삭제에 성공하였습니다.");
    centerRedirect();
  } catch (error) {
    console.log(error);
    showCustomAlert("삭제에 실패하였습니다.")
  }
};

// modify 처리
//파일 지우는지 확인 여부
const fileName = ref();
const removeFileName = ref("");

const removeFileNameCheck = ref(false); //지울 파일이 있는지 여부
const imgChecker = ref(true); //최종 이미지는 1장이여야 함
const infoChecker = ref(true);
const infoDetailChecker = ref(true);

// 쓰레기통 삭제파일
const deleteCheckBtn = () => {
  fileName.value = !fileName.value
}

watch(fileName, debounce((current,old)=>{  
  if(current === removeFileNameCheck.value){    
    imgChecker.value = true;
  }else{
    imgChecker.value = false;
  }
}
),200);

watch(infoCopy, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    infoChecker.value = true;    
  }else{
    infoChecker.value = false;
  }
},200))

watch(infoDetailCopy, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    infoDetailChecker.value = true;
  }else{
    infoDetailChecker.value = false;
  }
},200))

//파일 처리 로직
const trainerModifyImg = ref([]);
const trainerImage = (e) => {
  const files = e.target.files;
  for (let i = 0; i < files.length; i++) {
    trainerModifyImg.value.push(files[i]);
  }
  removeFileNameCheck.value = true
  if(fileName.value === removeFileNameCheck.value){
    imgChecker.value = true;
  }
};

//modify 전송버튼
const TrainerModifySubmitBtn = async (e) => {
  e.preventDefault();
  ModifyTrainerData.value.info = infoCopy.value;
  ModifyTrainerData.value.infoDetail = infoDetailCopy.value;
  if (fileName.value === true) {
    removeFileName.value = ModifyTrainerData.value.fileName;
  }

  const CenterTrainerModifyData = new FormData();
  for (let i = 0; i < trainerModifyImg.value.length; i++) {
    CenterTrainerModifyData.append("trainerImg", trainerModifyImg.value[i]);
  }

  CenterTrainerModifyData.append("removeImg", removeFileName.value);
  CenterTrainerModifyData.append("trainerId", ModifyTrainerData.value.id);
  CenterTrainerModifyData.append("modifyInfo", ModifyTrainerData.value.info);
  CenterTrainerModifyData.append("modifyInfoDetail", ModifyTrainerData.value.infoDetail);
  CenterTrainerModifyData.append(
    "centerId",
    ModifyTrainerData.value.centerId
  );

  try {
    await axios.put(url + "center/trainerModify", CenterTrainerModifyData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    centerRedirect();
    showCustomAlert("수정이 완료되었습니다.");
  } catch (error) {
    console.log(error);
  }
};
</script>

<style scoped>

p{
  color: red
}
label{
    color: black;
}
h4{
    color: black;
    text-align: center;
}
.black-bg {
  background-color: #f5f5f5;
  padding: 20px;
  color: #fff;
  margin-top: 20px;
  margin-left: auto;
  margin-right: auto;
  width: 70%;
}

.white-bg {
  background-color: #fff;
  padding: 20px;
}

.centerSearchContainer {
  display: inline-block;
  margin-bottom: 1vh;
  width: 100%;
}

.centerSearchContainer label {
  display: inline-block;
  margin-right: 1rem;
}

.centerSearchContainer input[type="text"] {
  display: inline-block;
  width: 100%;
}

.centerSearchContainer button {
  padding: 5px 10px;
  background-color: #2196f3;
  color: #fff;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.centerSearchContainer input[type="text"],
.centerSearchContainer button {
    border: 1px solid #ccc;
    outline: none;
    height: 4vh;
    border-radius: 5px;
}

.form-container {
  margin-top: 20px;
}

.form-container h4 {
  margin-bottom: 10px;
}

.trainerInfo,
.trainerInfoDetail {
  margin-bottom: 20px;
}

.trainerInfo label,
.trainerInfoDetail label {
  font-weight: bold;
}

.trainerInfo input[type="text"],
.trainerInfoDetail textarea {
  width: 100%;
  padding: 5px;
}

.trainerInfoDetail textarea {
  height: 120px;
}

.trainerImgContainer {
  margin-bottom: 20px;
}

.trainerImgContainer h4 {
  margin-bottom: 10px;
}

.trainerCurrentImg img {
  min-width: 100%;
  max-width: 100%;
  margin-bottom: 10px;
}

.addTrainerImg {
  margin-bottom: 20px;
}

.addTrainerImg label {
  display: inline-block;
  padding: 8px 16px;
  background-color: #007bff;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.addTrainerImg input[type="file"] {
  display: none;
}

.addTrainerImg label:hover {
  background-color: #23527c;
}


.TrainerModifyBtn button {
  padding: 5px 10px;
  background-color: #2196f3;
  color: #fff;
  border: none;
  cursor: pointer;
  margin-right: 10px;
  transition: background-color 0.3s ease;
}

hr {
  margin-top: 20px;
  margin-bottom: 20px;
}

button {
  padding: 5px 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  width: 100%;
  margin-bottom: 0.5vh;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #23527c;
}

.TrainerModifyBtn .trainerDeleteBtn{
    background-color: red;
    transition: background-color 0.3s ease;
}

.TrainerModifyBtn .trainerDeleteBtn:hover {
  background-color: lightcoral;
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

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}


.trainerModifyDeleteBtn{
  color: black;
}


.deleteCheckBtnRed i {
  color: red;
}

.deleteCheckBtnBlue i {
  color: blue;
}
</style>