/* eslint-disable no-template-curly-in-string */
/* eslint-disable no-undef */
import React, {useState,useEffect,useCallback} from 'react';
import './App.css';
import axios from 'axios';
import {useDropzone} from 'react-dropzone'

const UserProfiles = () => {

  const [userProfiles,setUserProfiles] = useState([]);

  const fetchUserProfiles =  () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then((response) => {
      console.log(response);
      setUserProfiles(response.data);
    });
  }

  useEffect(() => {
    fetchUserProfiles();
  },[]);


  return userProfiles.map((userProfile,index) => {
    return (
      <div key={index}>
        <br/>
        <h1>{userProfile.username}</h1>
        <p>{userProfile.userProfileId}</p>
        {/* <h1>{userProfile.userProfileImageLink}</h1> */}
        <Dropzone {...userProfile}/>
        <br/>
      </div>
    );
  });

};

function Dropzone({ userProfileId}) {
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];

    console.log(file);

    const formdata = new FormData();

    formdata.append("file",file);

    axios.post("http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload",
      formdata,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }
    ).then(() => { 
      console.log("file got filled")

    }).catch(err => {
      console.log(err)
    });

  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop 4 da bois ...</p> :
          <p>Drag 'n' drop some images hoe , or click to select files</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
     <UserProfiles/>
     shit
    </div>
  );
}

export default App;
