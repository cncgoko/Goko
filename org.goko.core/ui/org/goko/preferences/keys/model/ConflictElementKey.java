package org.goko.preferences.keys.model;

import org.eclipse.jface.bindings.TriggerSequence;

/**
	 * Key for conflict model
	 */
	class ConflictElementKey{
		private ContextElement context;
		private TriggerSequence triggerSequence;
		
		/**
		 * @param context
		 * @param triggerSequence
		 */
		public ConflictElementKey(ContextElement context, TriggerSequence triggerSequence) {
			super();
			this.context = context;
			this.triggerSequence = triggerSequence;
		}
		/**
		 * @return the context
		 */
		public ContextElement getContext() {
			return context;
		}
		/**
		 * @param context the context to set
		 */
		public void setContext(ContextElement context) {
			this.context = context;
		}
		/**
		 * @return the triggerSequence
		 */
		public TriggerSequence getTriggerSequence() {
			return triggerSequence;
		}
		/**
		 * @param triggerSequence the triggerSequence to set
		 */
		public void setTriggerSequence(TriggerSequence triggerSequence) {
			this.triggerSequence = triggerSequence;
		}
		/** (inheritDoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((context == null) ? 0 : context.hashCode());
			result = prime * result + ((triggerSequence == null) ? 0 : triggerSequence.hashCode());
			return result;
		}
		/** (inheritDoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConflictElementKey other = (ConflictElementKey) obj;
			if (context == null) {
				if (other.context != null)
					return false;
			} else if (!context.equals(other.context))
				return false;
			if (triggerSequence == null) {
				if (other.triggerSequence != null)
					return false;
			} else if (!triggerSequence.equals(other.triggerSequence))
				return false;
			return true;
		}
		
	}