import {call, put} from 'redux-saga/effects'
import { userInfoActions } from '../slice/userInfoSlice'
import history from '../utils/history'
import api from '../utils/axiosInstance'

//프로필 처음엔 작성...



export function* updateUserInfoAsync(action) {
  const data = action.payload;
  console.log(data)

  const response = yield api.post(`/room/user`, data)

  alert("저장되었습니다.");
  console.log(response)
  history.go(0);
}

export function* getUserInfoAsync(action) {
  const user_id = action.payload;

  const response = yield api.get('room/user'
  )
}