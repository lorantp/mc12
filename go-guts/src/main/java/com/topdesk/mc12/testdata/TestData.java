package com.topdesk.mc12.testdata;

import com.google.inject.Injector;
import com.topdesk.mc12.common.BoardSize;
import com.topdesk.mc12.common.Game;
import com.topdesk.mc12.common.Player;
import com.topdesk.mc12.persistence.Backend;

public class TestData {
	public static void create(Injector injector) {
		TestData testData = new TestData(injector.getInstance(Backend.class));
		testData.createUsers();
	}
	
	private final Backend backend;
	
	private TestData(Backend backend) {
		this.backend = backend;
	}
	
	private void createUsers() {
		Player jorn = new Player(0, "Jorn", "jornh@topdesk.com");
		Player bernd = new Player(0, "Bernd", "berndj@topdesk.com");
		Player bart = new Player(0, "Bart", "barte@topdesk.com");
		
		backend.insert(jorn, bernd, bart, new Game(0, BoardSize.NINE, bart, bernd, 0, 0));
		backend.insert(bart);
	}
}
