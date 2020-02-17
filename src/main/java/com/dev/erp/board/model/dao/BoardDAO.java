package com.dev.erp.board.model.dao;

import java.util.List;

import com.dev.erp.board.model.vo.Board;
import com.dev.erp.board.model.vo.BoardCategory;

public interface BoardDAO {

	int insertBoardCategory(BoardCategory boardCategory);

	List<BoardCategory> selectBoardCategoryList();

	Board seletOneBoard(int boardNo);

	List<Board> selectBoardType();


	BoardCategory boardCategoryView(int categoryNo);

	

	
}