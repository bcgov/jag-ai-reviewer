import React, { useState } from "react";
import Dropzone from "react-dropzone";
import { DisplayBox, Button, Dropdown } from "shared-components";
import { v4 as uuidv4 } from "uuid";
import { MdDescription } from "react-icons/md";
import {submitFileForExtraction} from "./SimulateTransactionService"
import "./SimulateTransaction.scss"

function SimulateTransaction() {
  const [files, setFiles] = useState([])
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(true);
  const [showLoader, setShowLoader] = useState(false);

  const transactionId = uuidv4();

  const handleDrop = (droppedFiles) => {
    droppedFiles[0].data = {type: ""}
    setFiles(droppedFiles)
  }

  const selectFileType = (e) => {
    const newFiles = [...files];
    newFiles[0].data.type = e
    setFiles(newFiles)
  }

  const submitDocument = () => {
    submitFileForExtraction(files[0], transactionId)
    .then(res => console.log(res))
    .catch(e => console.log(e.message))
  }

  const generateDisplayBoxElement = (file) => {
    return (
      <>
        <div className="col d-flex align-items-center">
          <h4>{file.name}</h4>
        </div>
        <div className="col">
          <Dropdown 
            items={["RCC","ABC"]}
            onSelect={(e) => selectFileType(e)}
          />
        </div>
        <div className="col">
        </div>
      </>
    )
  }

  return (
    <div className="simulate-transaction-ct">
      <Dropzone
        onDrop={(droppedFiles) => handleDrop(droppedFiles)}
      >
        {({ getRootProps, getInputProps }) => (
                  <div
                    data-testid="dropdownzone"
                    {...getRootProps({ className: "dropzone-outer-box" })}
                  >
                    <div className="dropzone-inner-box">
                      <input {...getInputProps()} />
                      <span>
                        <h2 className="text-center-alignment">
                          Drag and drop or&nbsp;
                          <span className="file-href">choose documents</span>
                        </h2>
                      </span>
                    </div>
                  </div>
                )}
      </Dropzone>
      <br />
      
      {files.length > 0 && (
        <>
          <h2>Uploaded Document</h2>
          <br />
          {files.map(file => (
            <div key={file.name}>
              <DisplayBox
                styling="bcgov-border-background bcgov-display-file"
                icon={
                  <div style={{ color: "rgb(252, 186, 25)" }}>
                    <MdDescription size={32} />
                  </div>
                }
                element={generateDisplayBoxElement(file)}
              />
            </div>
          ))}
        </>
      )}
      <br />
      <Button 
        label="Submit"
        styling="bcgov-normal-blue btn"
        onClick={submitDocument}
      />
    </div>
  )
}

export default SimulateTransaction;