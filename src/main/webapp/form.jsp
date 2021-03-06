<%
/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.ResourceBundle.Control" %>
<%@ page import="com.ibm.g11n.pipeline.client.ServiceAccount" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundle" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl.LookupMode" %>
<%@ page import="com.ibm.globalization.Globalization" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
//ResourceBundle res=ResourceBundle.getBundle( "com.ibm.health", request.getLocale());
ServiceAccount account = ServiceAccount.getInstance();
Control control = CloudResourceBundleControl.getInstance(account, CloudResourceBundleControl.LookupMode.LOCAL_THEN_REMOTE);
ResourceBundle res = ResourceBundle.getBundle("com.ibm.health", request.getLocale(), control);
%>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>
    <%=res.getString("product")%>
  </title>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.6.0/bootstrap-table.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/css/bootstrap-datepicker.standalone.min.css">

  <style type="text/css">
  <%@ include file="css/grid.css" %>
  <%@ include file="css/bookclub.css" %>
  </style>
</head>

<body>

  <nav id="myNavbar" class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbarCollapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">
          <%=res.getString("product")%>
        </a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="nav navbar-nav">
          <li>
            <a href="index.jsp">
              <%=res.getString("home")%>
            </a>
          </li>
          <li class="active">
            <a href="form.jsp">
              <%=res.getString("start")%>
                   </a>
                 </li>
               </ul>
             </div>
           </div>
         </nav>
         <div class="container">

           <div class="page-header">
             <h1><small><%=res.getString("explore_health")%></small></h1>
           </div>

           <label for="tranlslation" class="control-label"><%=res.getString("enable_translation")%></label>
           <div class="form-group">
           <select class="selectpicker form-control" data-width="auto" id="translation">
             <option value="true">
             <%=res.getString("yes")%>
             </option>
             <option value="false" selected="selected">
             <%=res.getString("no")%>
             </option>
           </select>
           </div>


    <label for="locationList" class="control-label"><%=res.getString("region_list")%></label>
    <div class="form-group">
      <select class="selectpicker form-control" data-width="auto" id="locationList" name="locationList" title='<%=res.getString("select_list")%>'>
        <option data-hidden="true"></option>

      </select>

    </div>

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title"><%=res.getString("panel_alerts")%></h3>
      </div>

             <div class="panel-body">
               <div class="panel-group" id="accordionPanel">
                 <div class="accordion" id="accordion" >
                       <div class="panel panel-default template" style="display: none;">
                         <div class="panel-heading">
                           <h4 class="panel-title">
							<a class="accordion-toggle" id="anchor0" data-toggle="collapse" data-parent="#accordion" href="#accordion-item"></a>
							</h4>
                          </div>
                          <div id="accordion-item" class="panel-collapse collapse" data-station="" data-number=0>
                  <div class="panel-body">

                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("health_issues")%></span>
                             <table data-toggle="table" class="table" id="table-conditions">
                        </table>
                      </div>
                    </div>

                    <div class="row-fluid">
                  	<div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("medline")%></span>
                             <table data-toggle="table" class="table" id="table-medline">
                        </table>
                      </div>
                    </div>

                    <div class="row-fluid">
                  	<div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("news")%></span>
                             <table data-toggle="table" class="table" id="table-news">
                        </table>
                      </div>
                    </div>

                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("twitter")%></span>
                             <table data-toggle="table" class="table" id="table-tweets">
                        </table>
                      </div>
                    </div>

          <!--
                    <div class="row-fluid">
                      <div class="col-md-12 column-white">
                        <span class="label label-primary"><%=res.getString("sentiment")%></span>
                          <p>
                           <span class="badge badge-positive" id="badgePositive">0</span>
                          <br>
                          <span class="badge badge-neutral" id="badgeNeutral">0</span>
                          <br>
                          <span class="badge badge-negative" id="badgeNegative">0</span>
                        </p>
                      </div>
                    </div>
          -->

                  </div>
                </div>
              </div>

          </div>
        </div>
      </div>

    </div>
  </div>

  <div id='ajax_loader' style="position: fixed; left: 50%; top: 50%; display: none;">
    <img src="${pageContext.request.contextPath}/images/ajax-loader.gif" />
  </div>

  <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.3/js/bootstrap-select.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.6.0/bootstrap-table.min.js"></script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/js/bootstrap-datepicker.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.zh-CN.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.zh-TW.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.ja.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.fr.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.es.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.it.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.pt-BR.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.0/locales/bootstrap-datepicker.de.min.js"></script>

  <script type="text/javascript">
  <%@ include file = "js/eventsource.min.js" %>
  <%@ include file = "js/jqTagCloud.js" %>
  </script>

  <script>

  function linkFormatter(value, row, index) {
  	return '<a target="_blank" href="' + value + '">' + value + '</a>';
  }

  function setupAlerts(IdNum) {
  	// Remove all the entries from the table
    var tableId = '#table-conditions' + IdNum;
    var jsonData = $('#accordion-item' + IdNum).data('healthAlert');
   	var healthAlert = JSON.parse(jsonData);
   	var conditions = healthAlert.healthConditions;


    $(tableId).bootstrapTable({
      	columns: [
      		{
      			field: "name",
      			title: "<%=res.getString("alert")%>"
      		},
      		{
      			field: "type",
      			title: "<%=res.getString("type")%>"
      		}
      	]
     });

     $(tableId).bootstrapTable('load', []);

     var enable = $('#translation').val();

     for (var i = 0; i < conditions.length; ++i) {
        $(tableId).bootstrapTable('append', conditions[i]);
     }
  }

  function setupMedline(IdNum) {
  	// Remove all the entries from the table
    var tableId = '#table-medline' + IdNum;
    var jsonData = $('#accordion-item' + IdNum).data('healthAlert');
   	var healthAlert = JSON.parse(jsonData);
   	var enable = $('#translation').val();
   	var conditions = healthAlert.healthConditions;


    $(tableId).bootstrapTable({
      	columns: [
      		{
      			field: "name",
      			title: "<%=res.getString("type")%>"
      		},
      		{
      			field: "value",
      			title: "<%=res.getString("value")%>"
      		}
      	]
     });

     $(tableId).bootstrapTable('load', []);

     $.ajax({
        	url: 'Terms',
      		type: 'GET',
      		data: {condition: conditions[0].name,
      			   enable: enable},
        	success: function(data) {
        		$(tableId).bootstrapTable('append', data);
        	},
        	error: function(xhr) {
        		console.log('<%=StringEscapeUtils.escapeJavaScript(res.getString("connect_error"))%>');
        	},
        	dataType: 'json',
        	timeout: 100000,
      	});

  }

  function setupNews(IdNum) {
  	// Remove all the entries from the table
    var tableId = '#table-news' + IdNum;
    var jsonData = $('#accordion-item' + IdNum).data('healthAlert');
   	var healthAlert = JSON.parse(jsonData);
   	var conditions = healthAlert.healthConditions;

    $(tableId).bootstrapTable({
      	columns: [
      		{
      			field: "title",
      			title: "<%=res.getString("title")%>"
      		},
      		{
      			field: "url",
      			title: "<%=res.getString("url")%>",
      			formatter: linkFormatter
      		}
      	]
     });

     $(tableId).bootstrapTable('load', []);

     var location = $('#locationList').val();

     $.ajax({
        	url: 'News',
      		type: 'GET',
      		data: {condition: conditions[0].name,
      			  location: location},
        	success: function(data) {
        	    for (var i = 0; i < data.length; ++i) {
        			$(tableId).bootstrapTable('append', data[i]);
     			}
        	},
        	error: function(xhr) {
        		console.log('<%=StringEscapeUtils.escapeJavaScript(res.getString("connect_error"))%>');
        	},
        	dataType: 'json',
        	timeout: 100000,
      	});

  }

  function polarityFormatter(value) {
  	var span = "";

    if (value === "POSITIVE") {
    	span = "<span class='badge badge-positive'>+</span>";
    } else if (value === "NEGATIVE") {
        span = "<span class='badge badge-negative'>-</span>";
    }
    else {
        span = "<span class='badge badge-neutral'>?</span>";
    }

    return span;
  }


  function setupTwitterEventSource(IdNum) {
    if (typeof(EventSource) !== 'undefined') {
      // Remove all the entries from the table
      var tableId = '#table-tweets' + IdNum;

      $(tableId).bootstrapTable({
      	columns: [
      		{
      			field: "screenName",
      			title: "<%=res.getString("screen_name_table")%>"
      		},
      		{
      			field: "tweet",
      			title: "<%=res.getString("message_table")%>"
      		},
          {
            field: "sentiment",
            title: "<%=res.getString("sentiment_label")%>",
            formatter: "polarityFormatter"
          },
      		{
      			field: "date",
      			title: "<%=res.getString("date")%>"
      		}
      	]
      });

      $(tableId).bootstrapTable('load', []);

	    var enable = $('#translation').val();

      var jsonData = $('#accordion-item' + IdNum).data('healthAlert');
      var healthAlert = JSON.parse(jsonData);
      var conditions = healthAlert.healthConditions;
      var location = $('#locationList').val();

      // collect tweets for the first health condition
      var source = new EventSource('Tweet?condition=' + conditions[0].name + '&location=' + location +
      '&enable=' + enable);

      source.onmessage = function(event) {
        var tweet = JSON.parse(event.data);

        $(tableId).bootstrapTable('append', [{
        		screenName: tweet.screenName,
        		tweet: tweet.tweet.message,
            sentiment: tweet.tweet.sentiment,
        		date: tweet.tweet.date}]);
      };

      source.onerror = function(event) {
      	alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      };

      source.addEventListener('finished', function(event) {
      	source.close();
      },false);

      } else {
      	alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("sse_error"))%>');
    }
    return false;
  }


  function setupSentimentEventSource(IdNum) {
    if (typeof(EventSource) !== 'undefined') {
      var jsonData = $('#accordion-item' + IdNum).data('healthAlert');
      var healthAlert = JSON.parse(jsonData);
      var conditions = healthAlert.healthConditions;

	  var location = $('#locationList').val();

      var source = new EventSource('Sentiment?condition=' + conditions[0].name + '&location=' + location);

      source.onmessage = function(event) {
        var sentiment = JSON.parse(event.data);
        if (sentiment.sentiment == "positive") {
          $('#badgePositive' + IdNum).text(sentiment.count);
        } else if (sentiment.sentiment === "negative") {
          $('#badgeNegative' + IdNum).text(sentiment.count);
        } else if (sentiment.sentiment === "neutral") {
          $('#badgeNeutral' + IdNum).text(sentiment.count);
        }
      };

      source.onerror = function(event) {
      	alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("closed"))%>');
      };

      source.addEventListener('finished', function(event) {
        source.close();
      }, false);
    } else {
      alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("sse_error"))%>');
    }
    return false;
  }


  // Load the translated string to show in the table when there is no data
  $.extend($.fn.bootstrapTable.defaults, {
  	formatNoMatches: function() {
            return '<%=StringEscapeUtils.escapeJavaScript(res.getString("no_data"))%>';
        }
    });

  // wait for the DOM to be loaded
  $(document).ready(function() {

    $('#translation').change(function() {
      $('#locationList').trigger("change");
    });

    // When an accordion panel is opened make the request for the details
    $('#accordion').on('show.bs.collapse', function(e) {
      // Determine which accordion panel is open
      var IdNum = $('#' + e.target.id).data('number');
      setupAlerts(IdNum);
      setupTwitterEventSource(IdNum);
      // Remove call to support IBM Insights for Twitter
      //setupSentimentEventSource(IdNum);
      setupMedline(IdNum);
      setupNews(IdNum);
    });

    // Initialize the select picker list
    $('.selectpicker').selectpicker();

    // Add the select list choice
    $('#locationList').append(new Option('<%=res.getString("select_list")%>', 'select', true, true));

    $.ajax({
        	url: 'Locations',
      		type: 'GET',
      		beforeSend: function() {
      			// Show the loading gif
      			$('#ajax_loader').show();
      			// Hide the list of while we are updating
      			$('#accordion').hide();
      		},
        	success: function(data) {
        		for (var i = 0; i < data.length; ++i) {
        			$('#locationList').append(new Option(data[i].name, data[i].name, false, false));
        		}
        		// Show the list of stations
        		$('#accordion').show();
        	},
        	error: function(xhr) {
        		alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("connect_error"))%>');
        	},
        	complete: function() {
        		// Hide the loading gif
        		$('#ajax_loader').hide();

        		// Extract the value of the first option.
				//var sVal = $('select[name=locationList] option:first').val();
        		//$('select[name=locationList]').val(sVal);

        		$('#locationList').selectpicker('refresh');
        	},
        	dataType: 'json',
        	timeout: 100000,
      	});

    // Find the base template panel for the accordion
    var $template = $(".template");

    // Listen for the selection of the region list
    $('#locationList').change(function() {
      var location = $('#locationList').val();

      // If we have a selected location
      if(location !== "") {

        // remove all the old alerts
      	$('#accordion').empty();

      	// Call the servlet to get back the alerts for a location
      	$.ajax({
        	url: 'Alerts',
      		type: 'GET',
      		data: {location: location,
      			   hscore: '0.25',
      			   lscore: '0.25'},
      		beforeSend: function() {
      			// Show the loading gif
      			$('#ajax_loader').show();
      			// Hide the list of while we are updating
      			$('#accordion').hide();
      		},
        	success: function(data) {
        		for (var i = 0; i < data.length; ++i) {

        		    var $newPanel = $template.clone();
    				$newPanel.find(".collapse").removeClass("in");

    				$newPanel.find(".accordion-toggle").attr("id",  "anchor" + (i));

    				var date = new Date(data[i].date).toLocaleDateString();

      				$newPanel.find(".accordion-toggle").attr("href",  "#accordion-item" + (i))
      					.text(data[i].title + " \u2014 " + date);
      				$newPanel.find(".panel-collapse").data("number", i);
      				$newPanel.find(".panel-collapse").attr("id", "accordion-item" + (i)).addClass("collapse").removeClass("in");
					$newPanel.find("#badgePositive").attr("id", "badgePositive" + (i));
      				$newPanel.find("#badgeNeutral").attr("id", "badgeNeutral" + (i));
      				$newPanel.find("#badgeNegative").attr("id", "badgeNegative" + (i));
      				$newPanel.find("#table-tweets").attr("id", "table-tweets" + (i));
					$newPanel.find("#table-conditions").attr("id", "table-conditions" + (i));
					$newPanel.find("#table-news").attr("id", "table-news" + (i));
					$newPanel.find("#table-medline").attr("id", "table-medline" + (i));

      				var value = JSON.stringify(data[i]);
        		    $newPanel.find('#accordion-item' + (i)).data('healthAlert', value);

        		    $("#accordion").append($newPanel.fadeIn());
        		}
        		// Show the list of stations
        		$('#accordion').show();
        	},
        	error: function(xhr) {
        		alert('<%=StringEscapeUtils.escapeJavaScript(res.getString("connect_error"))%>');
        	},
        	complete: function() {
        		// Hide the loading gif
        		$('#ajax_loader').hide();
        	},
        	dataType: 'json',
        	timeout: 100000,
      	});

      }

    });

  });
  </script>
</body>

</html>
