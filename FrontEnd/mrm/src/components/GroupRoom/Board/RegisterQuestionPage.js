import React, { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector, shallowEqual } from "react-redux";
import { Link, useParams, useLocation, useNavigate } from 'react-router-dom';

import { Grid } from '@mui/material';
import { Box } from '@mui/system';

import UserPageIcon from "../../MyRoom/MyRoomItem/UserPageIcon";
import PageIcon from '../../MyRoom/MyRoomItem/PageIcon';
import GroupProfile from '../GroupRoomItem/GroupProfile';
import CalendarBox from '../../Calendar/Calendar';
import MenuBtn from '../GroupRoomItem/MenuBtn';
import GroupMemberList from '../GroupRoomItem/GroupMemberList';

import RegisterOrEditQuestion from "./RegisterOrEditQuestion";
import { questionArticleActions } from "../../../slice/questionArticleSlice";
import styled from "styled-components";
import RoomModal from "../../Modal/Group/RoomModal";


function RegisterQuestionPage (props) {

  const dispatch = useDispatch();
	const params = useParams();
  const groupId = params.groupId;
  const navigate = useNavigate();

  const {user, id, group, views, date, editDate, title, content, picture, status} = useSelector((state) =>
  ({
    user: state.userInfoReducers.user,
    group: state.groupInfoReducers.group,
    id: state.questionArticleReducers.id,
    views: state.questionArticleReducers.views,
    date: state.questionArticleReducers.date,
    editDate: state.questionArticleReducers.editDate,  
    title: state.questionArticleReducers.title,
    content: state.questionArticleReducers.content,
    picture: state.questionArticleReducers.picture,
    status: state.questionArticleReducers.status
  }), shallowEqual)

  // const formData = new FormData()

  const [TitleValue, setTitleValue] = useState('')
  const [ContentValue, setContentValue] = useState('')

  // 새 글인지 수정인지
  const [IsForUpdate, setIsForUpdate] = useState(false);
  const search = useLocation();

  //여기 체크
  useEffect(() => {
    const paramsSearch = new URLSearchParams(search).get('search');
    const isRegisterForEdit = paramsSearch.split("=")[1]

    if (isRegisterForEdit === 'true') {
      console.log('true')
      dispatch(questionArticleActions.fetchQuestionArticle(id))
      setIsForUpdate(true);
    } else {
      console.log('false')
    }
    setTitleValue(title);
    setContentValue(content);
  }, [id]);

  const onRegisterChange = (event) => {
    const { name, value } = event.target;
    dispatch(questionArticleActions.changeQuestionRegisterInput({ name: name, value: value }));
  };

  const [image, setImage] = useState({ name: "" })
  
  const onImageChange = (event) => {
    console.log("event ======", event)
    console.log("event.target", event.target)
    console.log("event.target.files[0]", event.target.files[0])
    setImage(()=>event.target.files[0])
    console.log("image", image)
  }

  const formdata = new FormData()

  const onSubmitQuestionArticle = (event) => {
    event.preventDefault();

    if (title === "" || title === null || title === undefined) {
      alert("제목을 작성하십시오.");
      return false;
    }
    if (
      content === "" ||
      content === null ||
      content === undefined
    ) {
      alert("내용을 작성하십시오.");
      return false;
    }

    // const formdata = new FormData();
    formdata.append('picture', image)
    const questionArticleForRegister = {
      title: title, 
      content: content, 
      groupId: groupId, 
      user_id: user.id, 
      picture: formdata,
      navigate: navigate,
    };

    const questionArticleForUpdate = {
      id: id, 
      title: title, 
      content: content, 
      groupId: groupId, 
      status: status, 
      user_id: user.id,
      picture: formdata, 
      navigate: navigate,
    };

    if (IsForUpdate) {
      console.log('업데이트 ㄱㄱ')
      dispatch(questionArticleActions.updateQuestionArticle(questionArticleForUpdate)); // 추가
    } else {
      console.log('작성 ㄱㄱ')
      dispatch(questionArticleActions.registerQuestionArticle(questionArticleForRegister));
    } 
  }

  const [isOpen, setIsOpen] = useState(false);
  
  const onClickButton = () => {
    setIsOpen(true);
  };

  return (
    <Grid container>
      <Box
        sx={{
          width: "5vw",
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          backgroundColor: "#4A4A4A",
        }}>
        <Box>
          <Link to={`/myroom`}><UserPageIcon user={user}/></Link>
        </Box>
        <Box
          sx={{
            width: "4vw",
            height: "5px",
            backgroundColor: "#FFFFFF",
            borderRadius: "10px"
          }}>
        </Box>
        <Box
          sx={{
            height: "100%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-between"
          }}>
          <Box>
            {user.myRooms.map((room, index) => {
              return (<Link to={`/group/`+room.id}><PageIcon room={room}/></Link>)
            })}
          </Box>
          <Box>
            <AppWrap>
              <Button onClick={onClickButton}>+</Button>
              {isOpen && (<RoomModal
                open={isOpen}
                onClose={() => {
                  setIsOpen(false);
                }}
              />)}
            </AppWrap>
          </Box>
        </Box>
      </Box>

      <Box
        sx={{
          width: "95vw",
          display: "flex",
          justifyContent: "space-between",
        }}>
        <Box
          sx={{
            width: "288px",
            height: "98vh",
            paddingY: "1vh",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            backgroundColor: "#ebe5d1",
          }}>
          <GroupProfile />
          {/* 해당 groupId의 경로로 이동할 수 있도록 변경해야함 */}
          <Link to={`/group/${groupId}`}><MenuBtn name={"Home"} /></Link>
          <Link to={`/group/${groupId}/chat`}><MenuBtn name={"채팅방"} /></Link>
          <Link to={`/group/${groupId}/openvidu`}><MenuBtn name={"화상채팅방"} /></Link>
          <Link to={`/group/${groupId}/board`}><MenuBtn name={"게시판"} /></Link>
          <Link to={`/group/${groupId}/question`}><MenuBtn name={"Q&A"} /></Link>
        </Box>
        
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}>

          <Box sx={{mt:5}}>
            <RegisterOrEditQuestion
              id={id}
              titleValue={title}
              contentValue={content}
              groupId={groupId}
              user_id={user.id}
              handleRegisterChange={onRegisterChange}
              onImageHandler={onImageChange}
              handleSubmit={onSubmitQuestionArticle}
              updateRequest={IsForUpdate}
              formData = {formdata}
              picture={image.name}
              status={status}/>
          </Box>
        </Box>   

        <Box
          sx={{
            width: "288px",
            height: "98vh",
            paddingY: "1vh",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            backgroundColor: "#ebe5d1",
          }}>
          <CalendarBox />
          <Box
            sx={{
              width: "250px",
              // height: "550px",
              height: "55vh",
              marginTop: "20px",
              paddingY: '20px',
              borderRadius: "30px",
              backgroundColor: "#FFFFFF",
              boxShadow: "5px 5px 8px rgba(0, 0, 0, 0.35)",
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center'
            }}>
              <Box
                sx={{
                  width: "250px",
                  // height: "550px",
                  height: "5vh",
                  margin: "0px auto",
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center'
                }}>
                <h3>그룹 인원</h3>
                <hr align="center" width="80%"/>   
              </Box>
              <Box
                sx={{
                  width: "250px",
                  // height: "550px",
                  height: "50vh",
                  display: 'flex',
                  flexDirection: 'column',
                  // justifyContent: 'center',
                  alignItems: 'center'
                }}>
                {group.users 
                ? group.users.map((user, index) => {
                  return (<GroupMemberList user={user}/>)
                })
                : <div></div>
                }
              </Box>
          </Box>
          <Box
            sx={{
              width: "250px",
              // height: "80px",
              height: "6vh",
              marginTop: "20px",
              borderRadius: "30px",
              backgroundColor: "#FFFFFF",
              border: '5px solid #c45c5c',
              boxShadow: "5px 5px 8px rgba(0, 0, 0, 0.35)",
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              ':hover': {
                cursor: 'pointer'
              }
            }}>
            <h2>로그아웃</h2>
          </Box>
        </Box>
      </Box>
    </Grid>
    

      
    


    
  )
}

export default RegisterQuestionPage;
const Button = styled.button`
  font-size: 40px;
  padding: 10px 20px;
  border: none;
  background-color: #ffffff;
  border-radius: 10px;
  color: black;
  font-weight: 200;
  cursor: pointer;
  &:hover {
    background-color: #fac2be;
  }
`;

const AppWrap = styled.div`
  text-align: center;
  margin: 50px auto;
`;