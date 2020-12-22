package a06a.e1;

import java.util.HashMap;
import java.util.Map;

public class SRServiceFactoryImpl implements SRServiceFactory {

	private abstract class BasicSRService implements SRService {

		protected int nextClientId = 0;
		protected Map<Integer, Pair<Boolean, Boolean>> clients = new HashMap<>();

		@Override
		public int enter() {
			this.clients.put(this.nextClientId, new Pair<>(false, false));
			return this.nextClientId++;
		}

		public abstract boolean canApply(int id);

		private boolean isPresent(int id) {
			return this.clients.keySet().contains(id);
		}

		private boolean isFree(int id) {
			return !this.clients.get(id).getX() && !this.clients.get(id).getY();
		}

		@Override
		public void goReceive(int id) {
			if (!this.isPresent(id) || !this.isFree(id) || !this.canApply(id)) {
				throw new IllegalStateException();
			}
			this.clients.replace(id, new Pair<>(false, true));
		}

		@Override
		public void goSend(int id) {
			if (!this.isPresent(id) || !this.isFree(id) || !this.canApply(id)) {
				throw new IllegalStateException();
			}
			this.clients.replace(id, new Pair<>(true, false));
		}

		@Override
		public void exit(int id) {
			if (!(this.clients.get(id).getX() || this.clients.get(id).getY())) {
				throw new IllegalStateException();
			}
			this.clients.remove(id);
		}

	}

	@Override
	public SRService createMaximumAccess() {

		return new BasicSRService() {
			@Override
			public boolean canApply(int id) {
				return true;
			}
		};
	}

	@Override
	public SRService createWithNoConcurrentAccess() {

		return new BasicSRService() {
			@Override
			public boolean canApply(int id) {
				return !this.clients.values().stream().anyMatch(x -> x.getX() || x.getY());
			}
		};

	}

	@Override
	public SRService createManyReceiveAndMaxTwoSend() {
		return new BasicSRService() {

			public boolean canApply(int id) {
				return !(this.clients.values().stream().filter(x -> x.getX()).count() == 2);
			}
		};
	}
}
