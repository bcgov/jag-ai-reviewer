import React, { useState } from "react";
import PropTypes from "prop-types";
import { VscJson } from "react-icons/vsc";
import AddBoxIcon from "@material-ui/icons/AddBox";
import CloseIcon from "@material-ui/icons/Close";
import IconButton from "@material-ui/core/IconButton";
import Tooltip from "@material-ui/core/Tooltip";
import { MdDeleteForever, MdEdit, MdLibraryAdd } from "react-icons/md";

import "./DocumentList.scss";
import { getDocumentTypeConfigurations } from "./DocumentService";

export default function DocumentList({ configurations, setters, onDelete }) {
  const { showAdd, setShowAdd, setNewConfigInput, setIsNew, setIsUpdate } = setters;

  const handleShowAdd = () => {
    setShowAdd(true);
    setIsNew(true);
    setIsUpdate(false);
    setNewConfigInput("");
  };

  const handleUpdate = (docType) => {
    setShowAdd(true);
    setIsUpdate(true);
    setIsNew(false);
    getDocumentTypeConfigurations(docType).then((config) => {
      setNewConfigInput(JSON.stringify(config[0], null, 1));
    });
  };

  return (
    <div className="document-type-list">
      <div className="table-header">
        <span className="d-none d-sm-inline col-sm-3">Document Type</span>
        <span className="d-none d-sm-inline col-sm-3">Document Description</span>
        <span className="d-none d-sm-inline col-sm-3">Project ID</span>
        <span className="d-none d-sm-inline col-sm-3">Action</span>
      </div>
      <ul>
        {configurations.map((configuration) => (
          <li key={configuration.id}>
            <span className="col-12 d-inline d-sm-none label">
              <VscJson size="24" color="#FCBA19" strokeWidth="1" />
              {configuration.id}
            </span>

            <span className="col-6 d-inline d-sm-none">Document Type</span>
            <span className="col-6 col-sm-3 label">{configuration.documentType.type}</span>
            <span className="col-6 d-inline d-sm-none">Document Description</span>
            <span className="col-6 col-sm-3 label">{configuration.documentType.description}</span>
            <span className="col-6 d-inline d-sm-none">Project ID</span>
            <span className="col-6 col-sm-3 label">{configuration.projectId}</span>
            <span className="col-12 col-sm-3 pull-right">
              <IconButton
                data-testid="update-btn"
                size="small"
                className="ai-reviewer-icon-button"
                onClick={() => handleUpdate(configuration.documentType.type)}
              >
                <MdEdit size="24" color="#FCBA19" />
              </IconButton>
              <span tabIndex={-1} onClick={(e) => onDelete(configuration.id)} data-testid={"delete-" + configuration.id}>
                <MdDeleteForever size="24" className="delete-icon" />
              </span>
            </span>
          </li>
        ))}
        {!showAdd && (
          <li>
            <span className="col add-row">
              <Tooltip title="Add a new document type configuration">
                <IconButton data-testid="add-btn" size="small" className="ai-reviewer-icon-button" onClick={handleShowAdd}>
                  <AddBoxIcon className="ai-reviewer-icon" />
                </IconButton>
              </Tooltip>
            </span>
          </li>
        )}
      </ul>
    </div>
  );
}

DocumentList.propTypes = {
  configurations: PropTypes.arrayOf(PropTypes.object),
  setters: PropTypes.object,
  onDelete: PropTypes.func.isRequired,
};

DocumentList.defaultProps = {
  configurations: [],
  setters: {
    setNewConfigInput: () => {},
    setShowAdd: () => {},
    setIsNew: () => {},
    setIsUpdate: () => {},
  },
};
