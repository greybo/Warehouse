package com.stock.utils;

import com.stock.fragment.AllListFragment;
import com.stock.fragment.DetailsFragment;

/**
 * Created by m on 03.08.2017.
 */

public class StockConstants {
    public final static String TAG_ALLLISTFRAGMENT = AllListFragment.class.getSimpleName();
    public final static String TAG_DETAILFRAGMENT = DetailsFragment.class.getSimpleName();
    public final static String CONST_COMING = "coming";
    public final static String CONST_OUTPUT = "output";
    public final static int HANDLER_RESULT_OK = 10101;
    public final static int HANDLER_RESULT_LIST = 10102;
    public final static int HANDLER_NOT_FOUND = 10103;
    public final static int HANDLER_RESULT_ERR = 10104;

    public final static int HANDLER_USER_EXSIST_OK = 10201;
    public final static int HANDLER_USER_NOT_FOUND = 10202;
    public final static int HANDLER_USER_NEW_CREATE = 10203;
    public final static int HANDLER_USER_RESULT_ERR = 10204;
}
