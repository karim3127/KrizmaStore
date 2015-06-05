<?php require_once("st_inc/session.php"); ?>
<?php confirm_logged_in(); ?>
<?php require_once("st_inc/connection.php"); ?>
<?php require_once("st_inc/functions.php"); ?>

<?php
	$user_id = $_SESSION['user_id'];
		$query = "SELECT * FROM user WHERE id = '{$user_id}' LIMIT 1";
		$result = mysql_query($query, $connection);
		confirm_query($result);
		
			$found_user = mysql_fetch_array($result);
			$user_role = $found_user['user_role'];
			
	// START FORM PROCESSING
	if (isset($_POST['submit'])) { // Form has been submitted.
		
		if($user_role == 1) {
		
		if($_FILES["image"]["size"] > 0){
		
		$allowedExts = array("jpg", "jpeg", "gif", "png", "JPG", "JPEG", "GIF", "PNG");
		$extension = end(explode(".", $_FILES["image"]["name"]));
			if ((($_FILES["image"]["type"] == "image/gif")
			|| ($_FILES["image"]["type"] == "image/jpeg")
			|| ($_FILES["image"]["type"] == "image/png")
			|| ($_FILES["image"]["type"] == "image/pjpeg"))
			&& ($_FILES["image"]["size"] < 204800)
			&& in_array($extension, $allowedExts))
				  {
					if ($_FILES["image"]["error"] > 0){
					$error_message = $_FILES["image"]["error"];
					} else{
				
						if (file_exists("uploads/" . $_FILES["image"]["name"])) {
						  $error_message = $_FILES["image"]["name"] . " " . $LANG['image_exist'];
						} else {
							  if(move_uploaded_file($_FILES["image"]["tmp_name"],
							  "uploads/" . $_FILES["image"]["name"])) {  
							  // success
							  // echo "Stored in: " . "upload/" . $_FILES["file"]["name"];
							  $image_name = mysql_prep($_FILES["image"]["name"]);
							  } else {
							  $error_message = $LANG['x_upload'];
							  }
						}
					}
				 }
			else {
			  $error_message =  $LANG['upload_error'];
			  }
		}
		// perform validations on the form data
		if((!isset($_POST['pr_code'])) || (empty($_POST['pr_code']))){
			$error_message = $LANG['code_error'];
		} elseif ((!isset($_POST['pr_name'])) || (empty($_POST['pr_name']))) {
			$error_message = $LANG['description_error'];
		} elseif((!isset($_POST['quantity'])) && (!is_numeric($_POST['quantity']))) {
			$error_message = $LANG['quantity_error'];
		} elseif((!isset($_POST['unit'])) || (empty($_POST['unit']))) {
			$error_message = $LANG['unit_error'];
		} elseif((!isset($_POST['alert_qt'])) || (empty($_POST['alert_qt']))) {
			$error_message = $LANG['alert_qt_error'];
		}

		$pr_code = mysql_prep($_POST['pr_code']);
		$pr_name = mysql_prep($_POST['pr_name']);
		$quantity = mysql_prep($_POST['quantity']);
		$unit = mysql_prep($_POST['unit']);
		$alert_qt = mysql_prep($_POST['alert_qt']);
		
		$query = "SELECT * FROM products WHERE pr_code = '{$pr_code}' OR pr_name = '{$pr_name}' LIMIT 1";
		$result = mysql_query($query, $connection);
		confirm_query($result);
			
				$found_product = mysql_fetch_array($result);
				if( ($pr_code == $found_product['pr_code']) && ($pr_name == $found_product['pr_name']) ) { 
					$error_message = $LANG['product_exist']; 
				} elseif($pr_code == $found_product['pr_code']) { 
					$error_message = $LANG['code_exist']; 
				} elseif($pr_name == $found_product['pr_name']) { 
					$error_message = $LANG['description_exist']; 
				}

		
		if ( !isset($error_message) ) {
			// Check database to see if username and the hashed password exist there.
			if(isset($image_name)) {
			$query = "INSERT INTO products(
						pr_code, pr_name, quantity, unit, alert_quantity, pr_image
					) VALUES (
						'{$pr_code}', '{$pr_name}', '{$quantity}', '{$unit}', '{$alert_qt}', '{$image_name}'
					)"; 
			} else {
			$query = "INSERT INTO products(
						pr_code, pr_name, quantity, unit, alert_quantity
					) VALUES (
						'{$pr_code}', '{$pr_name}', '{$quantity}', '{$unit}', '{$alert_qt}'
					)"; 	
			}
			$result = mysql_query($query, $connection);
			if ($result) {
				// Success!
				$message_success = $LANG['product_added'];
			} else {
				// Display error message.
				$error = "<p>{$LANG['product_failed']}</p>";
				$error .= "<p>" . mysql_error() . "</p>";
			}
		} 
	} else { 
	$alert_message = $LANG['view_only'];
	}
		
	} else { 
	// Form has not been submitted.
			
	}
?>
<?php include("header.php"); ?>
<div id="body_section">
<?php  if(isset($message_success)) { echo "<div class=\"green_bar\">" . $message_success . "</div>"; } ?>
<?php  if(isset($error_message)) { echo "<div class=\"red_bar\">" . $error_message . "</div>"; } ?>
<?php  if(isset($error)) { echo "<div class=\"red_bar\">" . $error . "</div>"; } ?>
<?php  if(isset($alert_message)) { echo "<div class=\"yellow_bar\">" . $alert_message . "</div>"; } ?>
  <div id="body_section_inner">
<div class="contentPageWrapper">
	 
    <!-- register form -->
    <div class="pageSectionMain ui-corner-all">
        <div class="pageSectionMainInternal">
            <div id="pageHeader">
                <h2><?php echo $LANG['new_product']; ?></h2>
            </div>
            <div>
                <p class="introText">
                    <?php echo $LANG['enter_product_details'] ." ".$LANG['required_fields']; ?>             </p>
                    <script type='text/javascript' src='http://code.jquery.com/jquery-1.7.2.min.js?ver=3.4.1'></script>
                    <script type="text/javascript" src="js/gen_validatorv4.js"></script>
                <form enctype="multipart/form-data" class="international" method="post" action="<?php $PHP_SELF ?>" id="form-join" name="addproduct">
                <input type="hidden" name="MAX_FILE_SIZE" value="204800">
                    <ul>
                        <li class="field-container"><label for="pr_code">
                                <span class="field-name"><?php echo $LANG['product_code']; ?></span>
                                <input type="text" value="<?php if(isset($_POST['pr_code'])) { echo $_POST['pr_code']; } ?>" id="pr_code" name="pr_code" class="uiStyle text ui-widget-content ui-corner-all" onfocus="showHideTip(this);"> *</label>
                            <div id="pr_codeTip" class="hidden formTip">
                                <?php echo $LANG['pr_code_tip']; ?>                            </div>
                        </li>
                        
                        <li class="field-container"><label for="pr_name">
                                <span class="field-name"><?php echo $LANG['product_description']; ?></span>
                                <input type="text" value="<?php if(isset($_POST['pr_name'])) { echo $_POST['pr_name']; } ?>" id="pr_name" name="pr_name" class="uiStyle text ui-widget-content ui-corner-all" onfocus="showHideTip(this);">  *</label>
                            <div id="pr_nameTip" class="hidden formTip">
                                <?php echo $LANG['pr_name_tip']; ?>                            </div>
                        </li>
                        
                        <li class="field-container"><label for="quantity">
                                <span class="field-name"><?php echo $LANG['quantity']; ?></span>
                                <input type="text" value="<?php if(isset($_POST['quantity'])) { echo $_POST['quantity']; } ?>" id="quantity" name="quantity" class="uiStyle text ui-widget-content ui-corner-all" onfocus="showHideTip(this);"> *</label>
                            <div id="quantityTip" class="hidden formTip">
                               <?php echo $LANG['quantity_tip']; ?>                             </div>
                        </li>
                        
                        <li class="field-container"><label for="unit">
                                <span class="field-name"><?php echo $LANG['product_unit']; ?></span>
                                <input type="text" value="<?php if(isset($_POST['unit'])) { echo $_POST['unit']; } ?>" id="unit" name="unit" class="uiStyle text ui-widget-content ui-corner-all" onfocus="showHideTip(this);"> *</label>
                            <div id="unitTip" class="hidden formTip">
                                <?php echo $LANG['unit_tip']; ?>                            </div>
                        </li> 
                        <li class="field-container">
                            <label for="alert_qt">
                                <span class="field-name"><?php echo $LANG['alert_quantity']; ?></span>
                                <input type="text" value="<?php if(isset($_POST['alert_qt'])) { echo $_POST['alert_qt']; } ?>" id="alert_qt" name="alert_qt" class="uiStyle text ui-widget-content ui-corner-all" onfocus="showHideTip(this);"> *</label>
                            <div id="alert_qtTip" class="hidden formTip">
                                <?php echo $LANG['alert_qt_tip']; ?> 
                            </div>
                        </li>
                        
                        <li class="field-container">
                        <div class="upload">
    <label for="realupload"><span class="field-name"><?php echo $LANG['upload_image']; ?> </span>
    <div class="fakeupload">
        <input type="text" name="fakeupload" onfocus="showHideTip(this);" /> <!-- browse button is here as background -->
    </div>
    <input type="file" name="image" id="realupload" class="realupload" onchange="this.form.fakeupload.value = this.value;" onfocus="showHideTip(this);" /> 
    </label> </div>
    <div id="realuploadTip" class="hidden formTip">
                                <?php echo $LANG['image_tip']; ?> 
                            </div>
</li>
                        <li class="field-container">
                            <span class="field-name"></span>
                            <input type="submit" name="submit" value="<?php echo $LANG['add_product']; ?>" class="submitInput ui-button ui-widget ui-state-default ui-corner-all" role="button">
                        </li> 
                    </ul>

                    <input type="hidden" value="1" name="submitme">
                </form>
                <script language="JavaScript">
                var frmvalidator  = new Validator("addproduct");
                frmvalidator.addValidation("pr_code","req","<?php echo $LANG['code_error']; ?>...");
                frmvalidator.addValidation("pr_name","req","<?php echo $LANG['description_error']; ?>...");
				frmvalidator.addValidation("quantity","req","<?php echo $LANG['quantity_error']; ?>...");
				frmvalidator.addValidation("quantity","num","<?php echo $LANG['quantity_error2']; ?>...");
				frmvalidator.addValidation("unit","req","<?php echo $LANG['unit_error']; ?>...");
				frmvalidator.addValidation("alert_qt","req","<?php echo $LANG['alert_qt_error']; ?>...");
				frmvalidator.addValidation("alert_qt","num","<?php echo $LANG['alert_qt_error2']; ?>...");
				// frmvalidator.addValidation("image","req","Please select product image to upload...");
                </script>

                <div class="clear"></div>
            </div>
        </div>
    </div>
        <div class="clear"><!-- --></div>
</div>

<div class="clr"></div>

  </div>
  <div class="clear"></div>

</div>

<?php include("footer.php"); ?>