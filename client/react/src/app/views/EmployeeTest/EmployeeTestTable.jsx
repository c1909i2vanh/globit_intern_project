import React, { Component } from "react";
import {
  IconButton,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Icon,
  TablePagination,
  Button,
  Card,
  TextField,
  Checkbox
} from "@material-ui/core";
import DoneIcon from "@material-ui/icons/Done";
import CloseIcon from "@material-ui/icons/Close";
import MaterialTable, { MTableToolbar } from "material-table"
import { searchByPage, getItemById,deleteEmployee} from "./EmployeeTestService";
import EmployeeTestEditorDialog from "./EmployeeTestEditorDialog";
import { Breadcrumb, ConfirmationDialog } from "egret";
import shortid from "shortid";
import AutoComplete from "@material-ui/lab/Autocomplete";
function MaterialButton(props) {
  const item = props.item;
  return (
    <div>
      <IconButton onClick={() => props.onSelect(item, 0)}>
        <Icon color="primary">edit</Icon>
      </IconButton>
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="error">dektee</Icon>
      </IconButton>
    </div>
  );
}

class EmployeeTestTable extends Component {
  state = {
    employeeId: "",
    keyword: "",
    rowsPerPage: 25,
    page: 0,
    item: {},
    userList: [],
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    selectAllItem: false,
    selectedList: [],
    totalElements: 0,
    roles: [],
    role: [],
    shouldOpenConfirmationDeleteAllDialog: false,
    active: "",
  };
  numSelected = 0;
  rowCount = 0;
  handleTextChange = (event) => {
    this.setState({ keyword: event.target.value }, function () { });
  };
  handleKeyPress = (event) => {
    if (event.key === "Enter") {
      this.search();
    }
  };
  setPage = page => {
    this.setState({ page }, function () {
      this.updatePageData();
    });
  };

  setRowsPerPage = event => {
    this.setState({ rowsPerPage: event.target.value, page: 0 }, function () {
      this.updatePageData();
    });
  };

  handleChangePage = (event, newPage) => {
    this.setPage(newPage);
  };

  editDataAfterGetData = (itemList) => {
    let activeFalse = <CloseIcon style={{ color: "red" }} />;
    let activeTrue = <DoneIcon style={{ color: "green" }} />;

    let itemListUpdate = this.state.itemList;
    let listLength = this.state.itemList?.length;
    for (let i = 0; i < listLength; i++) {
      itemListUpdate[i].active = (this.itemList[i].active === true) ? activeTrue : activeFalse;

    }
    this.setState({
      itemList: itemListUpdate,
    });
  };
  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {};
      searchObject.keyword = this.state.keyword;
      searchObject.pageIndex = this.state.page + 1;
      searchObject.pageSize = this.state.pageSize;
      searchObject.isActive = this.state.isActive;
      searchByPage(searchObject).then(({ data }) => {
        this.setState({
          itemList: [...data.content],
          totalElements: data.totalElements,
        });
        this.editDataAfterGetData(this.state.itemList);
      });
    });
  }
  updatePageData = () => {
    // getAllEmployees().then(({ data }) => this.setState({ userList: [...data] }));
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.pageIndex;
    searchObject.pageSize = this.state.pageSize;
    searchObject.roles = this.state.roles;
    searchObject.isActive = this.state.isActive;

    searchByPage(searchObject).then(({ data }) => {
      this.setState({
        itemList: [...data.content],
        totalElements: data.totalElements,
      },
        console.log(data)
      );
      this.editDataAfterGetData(this.state.itemList);
    });
  };
  handleDownload = () => {
    var blob = new Blob(["hello world"], {
      type : "text/plain;charset=utf-8"
    });
  };

  handleDialogClose = () => {
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      shouldOpenConfirmationDeleteAllDialog: false
    });
    this.updatePageData();
  };
  handleOkEditDialogClose = () => {
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      shouldOpenConfirmationDeleteAllDialog: false
    });
    this.updatePageData();
  };
  handleDeleteEmployee = (id) => {
    this.setState({
      id,
      shouldOpenConfirmationDialog: true
    });
  };
  handleEditEmployee = (item) => {
    getItemById(item.id).then((result) => {
      this.setState({
        item: result.data,
        shouldOpenEditorDialog: true,
      });
    });
  };
  setValue = (newValue) => {
    this.setState({
      active: newValue,
    }, function () {
      this.search();
    });
  };
  handleConfirmationResponse = () => {
    deleteEmployee(this.state.user).then(() => {
      this.handleDialogClose();
    });
  };

  componentDidMount() {
    this.updatePageData();
  }

  handleEditItem = (item) => {
    this.setState({
      item: item,
      shouldOpenEditorDialog: true
    });
  };
  handleDelete = (id) => {
    this.setState({
      id,
      shouldOpenConfirmationDialog: true
    });
  };
  // handleDeleteAll =(event)=>{
  //   this.han
  // };

  render() {
    let {
      keyword,
      rowsPerPage,
      page,
      item,
      itemList,
      userList,
      shouldOpenConfirmationDialog,
      shouldOpenEditorDialog
    } = this.state;
    let columns = [
      {
        title:"Action",
        field:"custom",
        align:"center",
        render:(rowData,method)=>(
          <MaterialButton
          item={rowData}
          onSelect={(rowData,method)=>{
            if(method ===0){
              this.props.history.push({
                pathname:"/a",
                state:{
                  rowsPerPage:25,
                  page:0,
                  keyword:"",
                }
              })
            }else if(method ===1){
              this.handleDelete(rowData.id);
            }else{
              alert("Call selected Here: "+rowData.id)
            }
          }}
          />          
        ),
      },
      {
        headerStyle: {
          paddingLeft: "3px",
        },
        cellStyle: {
          paddingLeft: "3px",
        },
        title: "STT",
        field: "code",
        width: "7%",
        render: (rowData) => page * rowsPerPage + (rowData.tableData.id + 1)
      },
      {
        title: "Employee Id",
        field: "id",
        width: "7%"
      },
      {
        title: "Employee Name",
        field: "name",
        width: "17%"
      },
      {
        title: "Employee Email",
        field: "email",
        width: "28%"
      },
      {
        title: "Employee Phone",
        field: "phone",
        width: "7%"
      },
      {
        title: "Employee Age",
        field: "age",
        width: "7%"
      }
    ];
    return (
      <div className="m-sm-30">
        <div className="mb-sm-30">
          <Breadcrumb routeSegments={[{ name: "Employee Table" }]} />
        </div>

        <TextField id="standard-basic" label="Search" style={{ marginLeft: "20px" }} />

        <Checkbox
          value="checkEmployId"
          inputProps={{ 'aria-label': 'Checkbox A' }}
          text="Mã nhân viên"
        />
        <span>Mã nhân viên</span>
        <Checkbox
          value="checkEmployName"
          inputProps={{ 'aria-label': 'Checkbox A' }}
          text="Tên  nhân viên"
        />
        <span>Tên Nhân viên</span>
        <Checkbox
          value="checkEmployEmail"
          inputProps={{ 'aria-label': 'Checkbox A' }}
          text="Mã nhân viên"
        />
        <span>Email</span>
        <Checkbox
          value="checkEmployPhone"
          inputProps={{ 'aria-label': 'Checkbox A' }}
          text="Mã nhân viên"
        />
        <span>Số điện thoại</span>
        <Checkbox
          value="checkEmployAge"
          inputProps={{ 'aria-label': 'Checkbox A' }}
          text="Tuổi nhân viên"
        />
        <span>Tuổi</span>
        <Button
          className="mb-16"
          variant="contained"
          color="primary"
          //onClick={() => this.setState({ shouldOpenEditorDialog: true })}
          style={{
            marginLeft: "10px",
            marginRight: "10px"
          }}
        >
          Tìm kiếm
        </Button>

        <Button
          className="mb-16"
          variant="contained"
          color="primary"
          style={{ marginRight: "10px" }}
          onClick={() => this.setState({
            title: "Add New Member",
            shouldOpenEditorDialog: true
          })}
        >
          Add New Member
        </Button>
        <Button
          className="mb-16"
          variant="contained"
          color="primary"
          style={{ marginRight: "20px" }}
        //onClick={() => this.setState({ shouldOpenEditorDialog: true })}
        >
          Xuất file excel
        </Button>




        <Card className="w-100 overflow-auto" elevation={6}>
          <Table className="crud-table" style={{ whiteSpace: "pre", minWidth: "750px" }}>
            <TableHead>
              <TableRow>
                <TableCell>STT</TableCell>
                <TableCell>Id</TableCell>
                <TableCell>Mã nhân viên</TableCell>
                <TableCell>Tên nhân viên</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Số điện thoại</TableCell>
                <TableCell>Tuổi</TableCell>
                <TableCell>Is Active</TableCell>
                <TableCell>Action</TableCell>



              </TableRow>
            </TableHead>
            <TableBody>
              {userList
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((user, index) => (
                  <TableRow key={shortid.generate()}>
                    <TableCell className="px-0" align="left">
                      {page * rowsPerPage + 1}
                    </TableCell>
                    <TableCell className="px-0" align="left">
                      {user.id}
                    </TableCell>
                    <TableCell className="px-0">
                      {user.code}
                    </TableCell>
                    <TableCell className="px-0" align="left">
                      {user.name}
                    </TableCell>

                    <TableCell className="px-0" align="left">
                      {user.email}
                    </TableCell>
                    <TableCell className="px-0" align="left">
                      {user.phone}
                    </TableCell>
                    <TableCell className="px-0" align="left">
                      {user.age}
                    </TableCell>
                    <TableCell className="px-0">
                      {user.isActive ? (
                        <small className="border-radius-4 bg-primary text-white px-8 py-2 ">
                          active
                        </small>
                      ) : (
                          <small className="border-radius-4 bg-light-gray px-8 py-2 ">
                            inactive
                          </small>
                        )}
                    </TableCell>
                    <TableCell className="px-0 border-none">
                      <IconButton
                        onClick={() =>
                          this.setState({
                            uid: user.id,
                            shouldOpenEditorDialog: true
                          })
                        }
                      >
                        <Icon color="primary">edit</Icon>
                      </IconButton>
                      <IconButton onClick={() => this.handleDeleteUser(user)}>
                        <Icon color="error">delete</Icon>
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
            </TableBody>
          </Table>

          <TablePagination
            className="px-16"
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={userList.length}
            rowsPerPage={rowsPerPage}
            page={page}
            backIconButtonProps={{
              "aria-label": "Previous Page"
            }}
            nextIconButtonProps={{
              "aria-label": "Next Page"
            }}
            onChangePage={this.handleChangePage}
            onChangeRowsPerPage={this.setRowsPerPage}
          />

          {shouldOpenEditorDialog && (
            <EmployeeTestEditorDialog
              handleClose={this.handleDialogClose}
              open={shouldOpenEditorDialog}
              uid={this.state.uid}
              title={"Update Member"}
            />
          )}
          {shouldOpenConfirmationDialog && (
            <ConfirmationDialog
              open={shouldOpenConfirmationDialog}
              onConfirmDialogClose={this.handleDialogClose}
              onYesClick={this.handleConfirmationResponse}
              text="Are you sure to delete?"
            />
          )}
        </Card>
      </div>
    );
  }
}

export default EmployeeTestTable;
