package service;

public class ErrorResult extends Result {
  private String message;
  public ErrorResult(String message){
    this.message = message;
  }

  public void setErrorMessage(String errorMessage) {
    this.message = errorMessage;
  }
  public String getErrorMessage(){
    return message;
  }
}
